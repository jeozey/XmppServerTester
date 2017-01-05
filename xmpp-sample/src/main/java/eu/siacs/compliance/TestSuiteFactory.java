package eu.siacs.compliance;

import eu.siacs.extensions.csi.ClientStateIndication;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import eu.siacs.compliance.suites.AbstractTestSuite;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.session.Extension;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.session.XmppSessionConfiguration;

public class TestSuiteFactory {

    public static AbstractTestSuite create(Class <? extends AbstractTestSuite> clazz, Jid jid, String password,String host,int port) throws AbstractTestSuite.TestSuiteCreationException, GeneralSecurityException {
    	//by jeo
    	TcpConnectionConfiguration tcpConfiguration = TcpConnectionConfiguration.builder()
                .hostname(host) // The hostname.
                .port(port) // The XMPP default port.
                .sslContext(getTrustAllSslContext()) // Use an SSL context, which trusts every server. Only use it for testing!
                .secure(true) // We want to negotiate a TLS connection.
                .build();

    	XmppSessionConfiguration configuration = XmppSessionConfiguration.builder()
                .extensions(Extension.of(ClientStateIndication.class))
                .initialPresence(null)
                .build();
    	//by jeo
        final XmppClient client = XmppClient.create(jid.getDomain(),configuration,tcpConfiguration);
//        final XmppClient client = XmppClient.create(jid.getDomain(),configuration);
        try {
            AbstractTestSuite testSuite = clazz.getDeclaredConstructor(XmppClient.class, Jid.class, String.class).newInstance(client, jid, password);
            return testSuite;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AbstractTestSuite.TestSuiteCreationException();
        }
    }
    protected static SSLContext getTrustAllSslContext() throws GeneralSecurityException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        }, new SecureRandom());
        return sslContext;
    }
}
