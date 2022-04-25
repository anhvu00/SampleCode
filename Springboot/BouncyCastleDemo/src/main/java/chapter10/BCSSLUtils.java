package chapter10;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.security.auth.x500.X500PrivateCredential;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;



/**
 * Utilities for creating SSL certificates
 * TODO: 
 * - move hard coded constants to properties
 */
public class BCSSLUtils 
{
    /**
     * Host name for our examples to use.
     */
    static final String HOST = "localhost";
    
    /**
     * Port number for our examples to use.
     */
    static final int PORT_NO = 8443;

    /**
     * Names and passwords for the key store entries we need.
     */
    public static final String SERVER_NAME = "server";
    public static final char[] SERVER_PASSWORD = "changeit".toCharArray();

    public static final String CLIENT_NAME = "client";
    public static final char[] CLIENT_PASSWORD = "changeit".toCharArray();

    public static final String TRUST_STORE_NAME = "trustStore";
    public static final char[] TRUST_STORE_PASSWORD = "changeit".toCharArray();
    
    public static char[] KEY_PASSWD = "changeit".toCharArray();
    
    private static final int VALIDITY_PERIOD = 7 * 24 * 60 * 60 * 1000; // one week
    public static String ROOT_ALIAS = "root";
    public static String INTERMEDIATE_ALIAS = "intermediate";
    public static String END_ENTITY_ALIAS = "end";
    
    /**
     * Create a KeyStore containing the a private credential with
     * certificate chain and a trust anchor.
     */
    public static KeyStore createCredentials(String keyPassword)
        throws Exception
    {
        KeyStore store = KeyStore.getInstance("JKS");

        store.load(null, null);
        
        X500PrivateCredential    rootCredential = BCSSLUtils.createRootCredential();
        X500PrivateCredential    interCredential = BCSSLUtils.createIntermediateCredential(rootCredential.getPrivateKey(), rootCredential.getCertificate());
        X500PrivateCredential    endCredential = BCSSLUtils.createEndEntityCredential(interCredential.getPrivateKey(), interCredential.getCertificate());
        
        store.setCertificateEntry(rootCredential.getAlias(), rootCredential.getCertificate());
        store.setKeyEntry(endCredential.getAlias(), endCredential.getPrivateKey(), keyPassword.toCharArray(), 
                new Certificate[] { endCredential.getCertificate(), interCredential.getCertificate(), rootCredential.getCertificate() });

        return store;
    }
    
    /**
     * Build a path using the given root as the trust anchor, and the passed
     * in end constraints and certificate store.
     * <p>
     * Note: the path is built with revocation checking turned off.
     */
    public static PKIXCertPathBuilderResult buildPath(
        X509Certificate  rootCert,
        X509CertSelector endConstraints,
        CertStore        certsAndCRLs)
        throws Exception
    {
        CertPathBuilder       builder = CertPathBuilder.getInstance("PKIX", "BC");
        PKIXBuilderParameters buildParams = new PKIXBuilderParameters(Collections.singleton(new TrustAnchor(rootCert, null)), endConstraints);
        
        buildParams.addCertStore(certsAndCRLs);
        buildParams.setRevocationEnabled(false);
        
        return (PKIXCertPathBuilderResult)builder.build(buildParams);
    }
    
    

    
    /**
     * Generate a X500PrivateCredential for the root entity.
     */
    public static X500PrivateCredential createRootCredential()
        throws Exception
    {
        KeyPair         rootPair = generateRSAKeyPair();
        X509Certificate rootCert = generateRootCert(rootPair);
        
        return new X500PrivateCredential(rootCert, rootPair.getPrivate(), ROOT_ALIAS);
    }

    public static void test1() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyPair         rootPair = generateRSAKeyPair();
        X509Certificate root = generateRootCert(rootPair);

        X500PrivateCredential intermediateCredential = createIntermediateCredential(rootPair.getPrivate(), root);

        X500PrivateCredential endEntityCredential = createEndEntityCredential(intermediateCredential.getPrivateKey(), intermediateCredential.getCertificate());

        System.out.println("ROOT ---------------------  ");
        JcaPEMWriter writer = new JcaPEMWriter(new PrintWriter(System.out));
        writer.writeObject(root);
        writer.flush();
        writer.write("Intermediate ---------------- \n");
        writer.writeObject(intermediateCredential.getCertificate());
        writer.flush();
        writer.write("\n");
        writer.flush();
        writer.writeObject(endEntityCredential.getCertificate());
        writer.close();
    }
    
    /**
     * Generate a X500PrivateCredential for the intermediate entity.
     */
    public static X500PrivateCredential createIntermediateCredential(
        PrivateKey      caKey,
        X509Certificate caCert)
        throws Exception
    {
        KeyPair         interPair = generateRSAKeyPair();
        X509Certificate interCert = generateIntermediateCert(interPair.getPublic(), caKey, caCert);
        
        return new X500PrivateCredential(interCert, interPair.getPrivate(), INTERMEDIATE_ALIAS);
    }
    
    /**
     * Generate a X500PrivateCredential for the end entity.
     */
    public static X500PrivateCredential createEndEntityCredential(
        PrivateKey      caKey,
        X509Certificate caCert)
        throws Exception
    {
        KeyPair         endPair = generateRSAKeyPair();
        X509Certificate endCert = generateEndEntityCert(endPair.getPublic(), caKey, caCert);
        
        return new X500PrivateCredential(endCert, endPair.getPrivate(), END_ENTITY_ALIAS);
    }
    
    /**
     * Create a random 1024 bit RSA key pair
     */
    public static KeyPair generateRSAKeyPair()
        throws Exception
	{
        KeyPairGenerator  kpGen = KeyPairGenerator.getInstance("RSA", "BC");
    
        kpGen.initialize(1024, new SecureRandom());
    
        return kpGen.generateKeyPair();
	}
    

    
    /**
     * Generate a sample V1 certificate to use as a CA root certificate
     */
    public static X509Certificate generateRootCertOld(KeyPair pair)
            throws Exception
    {

        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1WithRSAEncryption");
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
        AsymmetricKeyParameter privateKeyAsymKeyParam = PrivateKeyFactory.createKey(pair.getPrivate().getEncoded());

        ContentSigner signer = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(privateKeyAsymKeyParam);

        X509v1CertificateBuilder builder = new X509v1CertificateBuilder(
                new X500Name("CN=Test CA Certificate"),
                new BigInteger(64, new SecureRandom()),
                new Date(),
                new Date(System.currentTimeMillis() + VALIDITY_PERIOD),
                new X500Name("CN=Test CA Certificate"),
                SubjectPublicKeyInfo.getInstance(pair.getPublic().getEncoded())
        );

        X509CertificateHolder holder = builder.build(signer);

        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(holder);
    }


    public static X509Certificate generateRootCert(KeyPair pair)
            throws Exception
    {

        X509v1CertificateBuilder builder = new JcaX509v1CertificateBuilder(
                new X500Name("CN=Test CA Certificate"),
                new BigInteger(64, new SecureRandom()),
                new Date(),
                new Date(System.currentTimeMillis() + VALIDITY_PERIOD),
                new X500Name("CN=Test CA Certificate"),
                pair.getPublic()
        );

        ContentSigner signer = new JcaContentSignerBuilder("SHA1WithRSAEncryption").setProvider("BC").build(pair.getPrivate());

        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(builder.build(signer));
    }
    
    /**
     * Generate a sample V3 certificate to use as an intermediate CA certificate
     */
    public static X509Certificate generateIntermediateCertOld(PublicKey intKey, PrivateKey caKey, X509Certificate caCert)
        throws Exception
    {
        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1WithRSAEncryption");
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
        AsymmetricKeyParameter privateKeyAsymKeyParam = PrivateKeyFactory.createKey(caKey.getEncoded());

        ContentSigner signer = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(privateKeyAsymKeyParam);


        X509v3CertificateBuilder builder = new X509v3CertificateBuilder(
                new X500Name(caCert.getSubjectDN().getName()),
                new BigInteger(64, new SecureRandom()),
                new Date(),
                new Date(System.currentTimeMillis() + VALIDITY_PERIOD),
                new X500Name("CN=Test Intermediate Certificate"),
                SubjectPublicKeyInfo.getInstance(intKey.getEncoded())
        );
        builder.addExtension(Extension.authorityKeyIdentifier, false, new AuthorityKeyIdentifier(caCert.getPublicKey().getEncoded()));
        builder.addExtension(Extension.subjectKeyIdentifier, false, new SubjectKeyIdentifier(intKey.getEncoded()));
        builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(0));
        builder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));

        X509CertificateHolder holder = builder.build(signer);

        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(holder);
    }


    public static X509Certificate generateIntermediateCert(PublicKey intKey, PrivateKey caKey, X509Certificate caCert)
            throws Exception
    {

        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                caCert,
                new BigInteger(64, new SecureRandom()),
                new Date(),
                new Date(System.currentTimeMillis() + VALIDITY_PERIOD),
                new X500Name("CN=Test Intermediate Certificate"),
                intKey
        );

        JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
        builder.addExtension(Extension.authorityKeyIdentifier, false, extensionUtils.createAuthorityKeyIdentifier(caCert));
        builder.addExtension(Extension.subjectKeyIdentifier, false, extensionUtils.createSubjectKeyIdentifier(intKey));
        builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(0));
        builder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));

        ContentSigner signer = new JcaContentSignerBuilder("SHA1WithRSAEncryption").setProvider("BC").build(caKey);

        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(builder.build(signer));
    }

    /**
     * Generate a sample V3 certificate to use as an end entity certificate
     */
    public static X509Certificate generateEndEntityCertOld(PublicKey entityKey, PrivateKey caKey, X509Certificate caCert)
	    throws Exception
	{
        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1WithRSAEncryption");
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
        AsymmetricKeyParameter privateKeyAsymKeyParam = PrivateKeyFactory.createKey(caKey.getEncoded());

        ContentSigner signer = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(privateKeyAsymKeyParam);


        X509v3CertificateBuilder builder = new X509v3CertificateBuilder(
                new X500Name(caCert.getSubjectDN().getName()),
                new BigInteger(64, new SecureRandom()),
                new Date(),
                new Date(System.currentTimeMillis() + VALIDITY_PERIOD),
                new X500Name("CN=Test End Certificate"),
                SubjectPublicKeyInfo.getInstance(entityKey.getEncoded())
        );
        builder.addExtension(Extension.authorityKeyIdentifier, false, new AuthorityKeyIdentifier(caCert.getPublicKey().getEncoded()));
        builder.addExtension(Extension.subjectKeyIdentifier, false, new SubjectKeyIdentifier(entityKey.getEncoded()));
        builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
        builder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));

        X509CertificateHolder holder = builder.build(signer);

        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(holder);
	}


    public static X509Certificate generateEndEntityCert(PublicKey entityKey, PrivateKey caKey, X509Certificate caCert)
            throws Exception
    {
        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                caCert,
                new BigInteger(64, new SecureRandom()),
                new Date(),
                new Date(System.currentTimeMillis() + VALIDITY_PERIOD),
                new X500Name("CN=Test End Certificate"),
                entityKey
        );

        JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
        builder.addExtension(Extension.authorityKeyIdentifier, false, extensionUtils.createAuthorityKeyIdentifier(caCert));
        builder.addExtension(Extension.subjectKeyIdentifier, false, extensionUtils.createSubjectKeyIdentifier(entityKey));
        builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(0));
        builder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));

        ContentSigner signer = new JcaContentSignerBuilder("SHA1WithRSAEncryption").setProvider("BC").build(caKey);

        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(builder.build(signer));
    }
    
    /**
     * Create a key for use with AES.
     * 
     * @param bitLength
     * @param random
     * @return an AES key.
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static SecretKey createKeyForAES(
        int          bitLength,
        SecureRandom random)
        throws NoSuchAlgorithmException, NoSuchProviderException
    {
        KeyGenerator generator = KeyGenerator.getInstance("AES", "BC");
        
        generator.init(256, random);
        
        return generator.generateKey();
    }
    
    /**
     * Create an IV suitable for using with AES in CTR mode.
     * <p>
     * The IV will be composed of 4 bytes of message number,
     * 4 bytes of random data, and a counter of 8 bytes.
     * 
     * @param messageNumber the number of the message.
     * @param random a source of randomness
     * @return an initialised IvParameterSpec
     */
    public static IvParameterSpec createCtrIvForAES(
        int             messageNumber,
        SecureRandom    random)
    {
        byte[]          ivBytes = new byte[16];
        
        // initially randomize
        
        random.nextBytes(ivBytes);
        
        // set the message number bytes
        
        ivBytes[0] = (byte)(messageNumber >> 24);
        ivBytes[1] = (byte)(messageNumber >> 16);
        ivBytes[2] = (byte)(messageNumber >> 8);
        ivBytes[3] = (byte)(messageNumber);
        
        // set the counter bytes to 1
        
        for (int i = 0; i != 7; i++)
        {
            ivBytes[8 + i] = 0;
        }
        
        ivBytes[15] = 1;
        
        return new IvParameterSpec(ivBytes);
    }
    
    /**
     * Convert a byte array of 8 bit characters into a String.
     * 
     * @param bytes the array containing the characters
     * @param length the number of bytes to process
     * @return a String representation of bytes
     */
    public static String toString(
        byte[] bytes,
        int    length)
    {
        char[]	chars = new char[length];
        
        for (int i = 0; i != chars.length; i++)
        {
            chars[i] = (char)(bytes[i] & 0xff);
        }
        
        return new String(chars);
    }
    
    /**
     * Convert a byte array of 8 bit characters into a String.
     * 
     * @param bytes the array containing the characters
     * @return a String representation of bytes
     */
    public static String toString(
        byte[]	bytes)
    {
        return toString(bytes, bytes.length);
    }
    
    /**
     * Convert the passed in String to a byte array by
     * taking the bottom 8 bits of each character it contains.
     * 
     * @param string the string to be converted
     * @return a byte array representation
     */
    public static byte[] toByteArray(
        String string)
    {
        byte[]	bytes = new byte[string.length()];
        char[]  chars = string.toCharArray();
        
        for (int i = 0; i != chars.length; i++)
        {
            bytes[i] = (byte)chars[i];
        }
        
        return bytes;
    }
}
