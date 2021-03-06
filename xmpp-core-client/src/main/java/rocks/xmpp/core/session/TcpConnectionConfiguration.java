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

package rocks.xmpp.core.session;

import javax.net.SocketFactory;

/**
 * A configuration for a TCP connection.
 * It allows you to configure various connection settings for a TCP socket connection, most importantly the host address and port,
 * but also a whitespace keep-alive interval, a custom socket factory, a custom SSL context and compression methods.
 * <h3>Usage</h3>
 * In order to create an instance of this class you have to use the builder pattern as shown below.
 * <pre>
 * {@code
 * TcpConnectionConfiguration tcpConfiguration = TcpConnectionConfiguration.builder()
 *     .hostname("localhost")
 *     .port(5222)
 *     .sslContext(sslContext)
 *     .secure(false)
 *     .build();
 * }
 * </pre>
 * This class is immutable.
 *
 * @author Christian Schudt
 * @see rocks.xmpp.extensions.httpbind.BoshConnectionConfiguration
 * @see TcpConnection
 */
public final class TcpConnectionConfiguration extends ConnectionConfiguration {

    private static volatile TcpConnectionConfiguration defaultConfiguration;

    private final int keepAliveInterval;

    private final SocketFactory socketFactory;

    private TcpConnectionConfiguration(Builder builder) {
        super(builder);
        this.keepAliveInterval = builder.keepAliveInterval;
        this.socketFactory = builder.socketFactory;
    }

    /**
     * Creates a new builder for this class.
     *
     * @return The builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets the default configuration.
     *
     * @return The default configuration.
     */
    public static TcpConnectionConfiguration getDefault() {
        // Use double-checked locking idiom
        if (defaultConfiguration == null) {
            synchronized (TcpConnectionConfiguration.class) {
                if (defaultConfiguration == null) {
                    defaultConfiguration = builder().build();
                }
            }
        }
        return defaultConfiguration;
    }

    /**
     * Sets the default configuration.
     *
     * @param configuration The default configuration.
     */
    public static void setDefault(TcpConnectionConfiguration configuration) {
        synchronized (TcpConnectionConfiguration.class) {
            defaultConfiguration = configuration;
        }
    }

    @Override
    public final Connection createConnection(XmppSession xmppSession) {
        return new TcpConnection(xmppSession, this);
    }

    /**
     * Gets the whitespace keep-alive interval.
     *
     * @return The whitespace keep-alive interval.
     */
    public final int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    /**
     * Gets the socket factory.
     *
     * @return The socket factory.
     */
    public final SocketFactory getSocketFactory() {
        return socketFactory;
    }

    @Override
    public final String toString() {
        return "TCP connection configuration: " + super.toString();
    }

    /**
     * A builder to create a {@link TcpConnectionConfiguration} instance.
     */
    public static final class Builder extends ConnectionConfiguration.Builder<Builder> {

        private int keepAliveInterval;

        private SocketFactory socketFactory;

        private Builder() {
            // default values.
            secure(true);
            port(5222);
            keepAliveInterval(30);
        }

        /**
         * Sets the whitespace keep-alive interval in seconds. If the interval is negative, no whitespace will be sent at all.
         *
         * @param keepAliveInterval The whitespace keep-alive interval.
         * @return The builder.
         */
        public Builder keepAliveInterval(int keepAliveInterval) {
            this.keepAliveInterval = keepAliveInterval;
            return this;
        }

        /**
         * Sets a socket factory which creates the socket.
         * This can be useful if you want connect to the legacy SSL port (usually 5223) and the connection is encrypted right from the beginning.
         * <p>
         * However, usually, there's no need to set a custom socket factory.
         *
         * @param socketFactory The socket factory.
         * @return The builder.
         * @see #secure(boolean)
         */
        public Builder socketFactory(SocketFactory socketFactory) {
            this.socketFactory = socketFactory;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public TcpConnectionConfiguration build() {
            return new TcpConnectionConfiguration(this);
        }
    }
}
