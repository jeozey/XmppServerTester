/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Christian Schudt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package rocks.xmpp.extensions.chatstates;

import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.session.Manager;
import rocks.xmpp.core.session.XmppSession;
import rocks.xmpp.core.stanza.MessageEvent;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.extensions.chatstates.model.ChatState;
import rocks.xmpp.extensions.xhtmlim.model.Html;
import rocks.xmpp.im.chat.Chat;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * This class manages Chat State Notifications, which are used to communicate the status of a user in a chat session, thus indicating whether a chat partner is actively engaged in the chat, composing a message, temporarily paused, inactive, or gone.
 * Chat states can be used in the context of a one-to-one chat session or a multi-user chat room.
 * <p>
 * Because the Chat State protocol is relatively simple, the primary purpose of this manager is to enable or disable the Chat State protocol for Service Discovery purposes.
 * </p>
 * <p>
 * Furthermore it ensures that every sent message has a chat state notification as required by XEP-0085.
 * </p>
 * <h3>Sending Chat States</h3>
 * Setting your own chat state can either be done in a one-to-one chat session or a group chat.
 * <pre>
 * {@code
 * ChatStateManager chatStateManager = xmppSession.getManager(ChatStateManager.class);
 * chatStateManager.setChatState(ChatState.COMPOSING, chat);
 * }
 * </pre>
 * <h3>Receiving Chat States</h3>
 * If you want to react to chat states of your chat partner(s), just check for chat state extension and deal with it accordingly.
 * <pre>
 * {@code
 * ChatState chatState = message.getExtension(ChatState.class);
 * if (chatState == ChatState.COMPOSING) {
 *     // Contact is typing.
 * } else if (chatState == ChatState.PAUSED) {
 *     // Contact has paused typing.
 * }
 * }
 * </pre>
 *
 * @author Christian Schudt
 * @see <a href="http://xmpp.org/extensions/xep-0085.html">XEP-0085: Chat State Notifications</a>
 */
public final class ChatStateManager extends Manager {

    private final Map<Chat, ChatState> chatMap = new ConcurrentHashMap<>();

    private final Map<Jid, Boolean> contactSupportsChatStateNotifications = new ConcurrentHashMap<>();

    private final Consumer<MessageEvent> messageListener;

    private ChatStateManager(final XmppSession xmppSession) {
        super(xmppSession, true);
        this.messageListener = e -> {
            Message message = e.getMessage();
            // This protocol SHOULD NOT be used with message types other than "chat" or "groupchat".
            if (message.getType() == Message.Type.CHAT || message.getType() == Message.Type.GROUPCHAT) {
                // For outbound messages append <active/>.
                boolean containsChatState = message.hasExtension(ChatState.class);
                if (!e.isInbound()) {
                    // Append an <active/> chat state to every outbound content message (with <body> or <html> extension), if it doesn't contain a chat state yet
                    // and the recipient supports chat states or it is unknown if he supports them.
                    if (!containsChatState && (message.getBody() != null && !message.getBody().trim().equals("") || message.hasExtension(Html.class))) {
                        // If either support of chat states is unknown (== null) or it's known to be supported (== true), include an active chat state.
                        // (1. If the User desires chat state notifications, the message(s) that it sends to the Contact before receiving a reply MUST contain a chat state notification extension, which SHOULD be <active/>.)
                        Boolean isSupportedByPeer = contactSupportsChatStateNotifications.get(message.getTo());
                        if (isSupportedByPeer == null || isSupportedByPeer) {
                            message.addExtension(ChatState.ACTIVE);
                        }
                    }
                } else if (message.getType() != Message.Type.GROUPCHAT) {
                    // Check if the contact supports chat states and update the map. If it does, it must include a chat state extension:
                    // 2. If the Contact replies but does not include a chat state notification extension, the User MUST NOT send subsequent chat state notifications to the Contact.
                    // 3. If the Contact replies and includes an <active/> notification (or sends a standalone notification to the User), the User and Contact SHOULD send subsequent notifications
                    contactSupportsChatStateNotifications.put(message.getFrom(), containsChatState);
                }
            }
        };
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        xmppSession.addInboundMessageListener(messageListener);
        xmppSession.addOutboundMessageListener(messageListener);
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        xmppSession.removeInboundMessageListener(messageListener);
        xmppSession.removeOutboundMessageListener(messageListener);
    }

    /**
     * Sets the chat state for a chat. If this manager is disabled this method has no effect.
     *
     * @param chatState The chat state.
     * @param chat      The chat.
     * @return True, if the chat state has been sent; false, if it has not been sent (e.g. because it is known that the chat partner does not support chat states).
     */
    public final boolean setChatState(ChatState chatState, Chat chat) {
        if (!isEnabled()) {
            throw new IllegalStateException("Chat States aren't enabled. Please enable them before sending chat states.");
        }

        // Avoid repetition.
        // See XEP-0085 § 5.3 Repetition
        if (chatMap.put(Objects.requireNonNull(chat), Objects.requireNonNull(chatState)) == chatState) {
            return false;
        }

        Message message = new Message();
        message.addExtension(chatState);
        chat.sendMessage(message);
        return true;
    }

    @Override
    protected void dispose() {
        chatMap.clear();
        contactSupportsChatStateNotifications.clear();
    }
}
