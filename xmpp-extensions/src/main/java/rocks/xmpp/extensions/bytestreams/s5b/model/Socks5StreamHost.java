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

package rocks.xmpp.extensions.bytestreams.s5b.model;

import rocks.xmpp.addr.Jid;

/**
 * A SOCKS5 stream host interface which is shared by the XEP-0065 implementation and the XEP-0260 implementation.
 *
 * @author Christian Schudt
 */
public interface Socks5StreamHost {

    /**
     * Gets the IP address or DNS domain name of the StreamHost for SOCKS5 communication over TCP.
     *
     * @return The hostname.
     */
    String getHostname();

    /**
     * Gets the JabberID of the StreamHost for communication over XMPP.
     *
     * @return The JID.
     */
    Jid getJid();

    /**
     * Get the port on which to connect for SOCKS5 communication over TCP.
     *
     * @return The port.
     */
    int getPort();
}
