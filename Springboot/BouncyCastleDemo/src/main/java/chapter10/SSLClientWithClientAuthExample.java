package chapter10;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.*;

/**
 * SSL Client with client-side authentication.
 */
public class SSLClientWithClientAuthExample
    extends SSLClientExample
{
    /**
     * Create an SSL context with a KeyManager providing our identity
     */
    private static SSLContext createSSLContext()
        throws Exception
    {
        // set up a key manager for our local credentials
		KeyManagerFactory mgrFact = KeyManagerFactory.getInstance("SunX509");
		KeyStore clientStore = KeyStore.getInstance("PKCS12");
		clientStore.load(new FileInputStream("client-1.p12"), BC_SSLUtils.CLIENT_PASSWORD);
		mgrFact.init(clientStore, BC_SSLUtils.CLIENT_PASSWORD);


		TrustManagerFactory tmfc = TrustManagerFactory.getInstance("SunX509");
		KeyStore trustStore = KeyStore.getInstance("JKS");
		trustStore.load(new FileInputStream("trustStore-1.jks"), BC_SSLUtils.TRUST_STORE_PASSWORD);
        tmfc.init(trustStore);

		// create a context and set up a socket factory
		SSLContext sslContext = SSLContext.getInstance("TLS");

		sslContext.init(mgrFact.getKeyManagers(), tmfc.getTrustManagers(), null);
		
        return sslContext;
    }
    
    public static void main(
        String[] args)
        throws Exception
    {
		SSLContext       sslContext = createSSLContext();
		SSLSocketFactory fact = sslContext.getSocketFactory();
        SSLSocket        cSock = (SSLSocket)fact.createSocket(BC_SSLUtils.HOST, BC_SSLUtils.PORT_NO);

        doProtocol(cSock);
    }
}
