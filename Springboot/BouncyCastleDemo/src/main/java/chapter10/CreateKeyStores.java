package chapter10;



import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;

import javax.security.auth.x500.X500PrivateCredential;

/**
 * Create the various credentials for an SSL session
 */
public class CreateKeyStores extends BaseClass
{
    public static void main(String[] args)
        throws Exception
    {
        X500PrivateCredential    rootCredential = BCSSLUtils.createRootCredential();
        X500PrivateCredential    interCredential = BCSSLUtils.createIntermediateCredential(rootCredential.getPrivateKey(), rootCredential.getCertificate());
        X500PrivateCredential    endCredential = BCSSLUtils.createEndEntityCredential(interCredential.getPrivateKey(), interCredential.getCertificate());
        
        // client credentials
        KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
        
        keyStore.load(null, null);
        
        keyStore.setKeyEntry(BCSSLUtils.CLIENT_NAME, endCredential.getPrivateKey(), BCSSLUtils.CLIENT_PASSWORD, 
                new Certificate[] { endCredential.getCertificate(), interCredential.getCertificate(), rootCredential.getCertificate() });
        
        keyStore.store(new FileOutputStream(BCSSLUtils.CLIENT_NAME + ".p12"), BCSSLUtils.CLIENT_PASSWORD);
        
        // trust store for client
        keyStore = KeyStore.getInstance("JKS");
        
        keyStore.load(null, null);
        
        keyStore.setCertificateEntry(BCSSLUtils.SERVER_NAME, rootCredential.getCertificate());
        
        keyStore.store(new FileOutputStream(BCSSLUtils.TRUST_STORE_NAME + ".jks"), BCSSLUtils.TRUST_STORE_PASSWORD);
        
        // server credentials
        keyStore = KeyStore.getInstance("JKS");
        
        keyStore.load(null, null);
        
        keyStore.setKeyEntry(BCSSLUtils.SERVER_NAME, rootCredential.getPrivateKey(), BCSSLUtils.SERVER_PASSWORD,
                new Certificate[] { rootCredential.getCertificate() });
        
        keyStore.store(new FileOutputStream(BCSSLUtils.SERVER_NAME + ".jks"), BCSSLUtils.SERVER_PASSWORD);
    }
}
