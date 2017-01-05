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

package rocks.xmpp.extensions.amp.model.errors;

import rocks.xmpp.extensions.amp.model.AdvancedMessageProcessingError;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The implementation of the {@code <failed-rules/>} element in the {@code http://jabber.org/protocol/amp#errors} namespace.
 * <p>
 * One or more rules triggered the "error" action. This condition contains the triggered rules.
 * </p>
 * This class is immutable.
 *
 * @author Christian Schudt
 * @see <a href="http://xmpp.org/extensions/xep-0079.html">XEP-0079: Advanced Message Processing</a>
 * @see <a href="http://xmpp.org/extensions/xep-0079.html#schemas-err">XML Schema</a>
 */
@XmlRootElement(name = "failed-rules")
public final class FailedRules extends AdvancedMessageProcessingError {
}
