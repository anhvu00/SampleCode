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
        X500PrivateCredential    rootCredential = BC_SSLUtils.createRootCredential();
        X500PrivateCredential    interCredential = BC_SSLUtils.createIntermediateCredential(rootCredential.getPrivateKey(), rootCredential.getCertificate());
        X500PrivateCredential    endCredential = BC_SSLUtils.createEndEntityCredential(interCredential.getPrivateKey(), interCredential.getCertificate());
        
        // client credentials
        KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
        
        keyStore.load(null, null);
        
        keyStore.setKeyEntry(BC_SSLUtils.CLIENT_NAME, endCredential.getPrivateKey(), BC_SSLUtils.CLIENT_PASSWORD, 
                new Certificate[] { endCredential.getCertificate(), interCredential.getCertificate(), rootCredential.getCertificate() });
        
        keyStore.store(new FileOutputStream(BC_SSLUtils.CLIENT_NAME + ".p12"), BC_SSLUtils.CLIENT_PASSWORD);
        
        // trust store for client
        keyStore = KeyStore.getInstance("JKS");
        
        keyStore.load(null, null);
        
        keyStore.setCertificateEntry(BC_SSLUtils.SERVER_NAME, rootCredential.getCertificate());
        
        keyStore.store(new FileOutputStream(BC_SSLUtils.TRUST_STORE_NAME + ".jks"), BC_SSLUtils.TRUST_STORE_PASSWORD);
        
        // server credentials
        keyStore = KeyStore.getInstance("JKS");
        
        keyStore.load(null, null);
        
        keyStore.setKeyEntry(BC_SSLUtils.SERVER_NAME, rootCredential.getPrivateKey(), BC_SSLUtils.SERVER_PASSWORD,
                new Certificate[] { rootCredential.getCertificate() });
        
        keyStore.store(new FileOutputStream(BC_SSLUtils.SERVER_NAME + ".jks"), BC_SSLUtils.SERVER_PASSWORD);
    }
}
