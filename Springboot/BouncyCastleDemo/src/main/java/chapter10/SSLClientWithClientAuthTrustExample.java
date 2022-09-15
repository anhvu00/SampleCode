package chapter10;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * SSL Client with client-side authentication.
 */
public class SSLClientWithClientAuthTrustExample
    extends SSLClientExample
{
    /**
     * Create an SSL context with both identity and trust store
     */
    static SSLContext createSSLContext() 
        throws Exception
    {
        // set up a key manager for our local credentials
		KeyManagerFactory mgrFact = KeyManagerFactory.getInstance("SunX509");
		KeyStore clientStore = KeyStore.getInstance("PKCS12");

		InputStream isClient = SSLClientWithClientAuthTrustExample.class.getClassLoader().getResourceAsStream("store/client");
		clientStore.load(isClient, BC_SSLUtils.CLIENT_PASSWORD);

		mgrFact.init(clientStore, BC_SSLUtils.CLIENT_PASSWORD);
		
		// set up a trust manager so we can recognize the server
		TrustManagerFactory trustFact = TrustManagerFactory.getInstance("SunX509");
		KeyStore            trustStore = KeyStore.getInstance("JKS");
		
		InputStream isTrustStore = SSLClientWithClientAuthTrustExample.class.getClassLoader().getResourceAsStream("store/cacerts");
		trustStore.load(isTrustStore, BC_SSLUtils.TRUST_STORE_PASSWORD);
		
		trustFact.init(trustStore);
		
		// create a context and set up a socket factory
		SSLContext sslContext = SSLContext.getInstance("TLS");

		sslContext.init(mgrFact.getKeyManagers(), trustFact.getTrustManagers(), null);

		return sslContext;
    }
    
    public static void main(
        String[] args)
        throws Exception
    {
        SSLContext       sslContext = createSSLContext();
		SSLSocketFactory fact = sslContext.getSocketFactory();
        SSLSocket        cSock = (SSLSocket)fact.createSocket("localhost", 9020);

        doProtocol(cSock);
    }
}
