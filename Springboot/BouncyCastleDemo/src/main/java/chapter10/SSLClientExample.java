package chapter10;



import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.*;

/**
 * Basic SSL Client - using the '!' protocol.
 */
public class SSLClientExample extends BaseClass
{
    /**
     * Carry out the '!' protocol - client side.
     */
    static void doProtocol(
        Socket cSock)
        throws IOException
    {
        OutputStream     out = cSock.getOutputStream();
        InputStream      in = cSock.getInputStream();
        
        out.write(BC_SSLUtils.toByteArray("World"));
        out.write('!');
        
        int ch = 0;
        while ((ch = in.read()) != '!')
        {
            System.out.print((char)ch);
        }
        
        System.out.println((char)ch);
    }


    private static SSLContext createSslContext() throws Exception {

        TrustManagerFactory tmfc = TrustManagerFactory.getInstance("SunX509");
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("trustStore-1.jks"), BC_SSLUtils.TRUST_STORE_PASSWORD);
        tmfc.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmfc.getTrustManagers(), null);
        return sslContext;
    }


    public static void main(
        String[] args)
        throws Exception
    {
        //-Djavax.net.ssl.trustStore=trustStore.jks
        //SSLSocketFactory fact = (SSLSocketFactory)SSLSocketFactory.getDefault();
        SSLContext sslContext = createSslContext();
        SSLSocketFactory fact = sslContext.getSocketFactory();
        SSLSocket        cSock = (SSLSocket)fact.createSocket(BC_SSLUtils.HOST, BC_SSLUtils.PORT_NO);
        
        doProtocol(cSock);
    }
}
