package com.kyron.BouncyCastleDemo;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.security.auth.x500.X500PrivateCredential;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

/*
 * This class only contains the minimum functions to create self-signed certs and trust/keystore.
 * It is intended to use in kirby project
 */
public class BCSSLUtils {
	
	public static final int VALIDITY_PERIOD_MS = 7 * 24 * 60 * 60 * 1000; // one week in milliseconds
	public static final String ROOT_ALIAS = "root";
	public static final String INTERMEDIATE_ALIAS = "intermediate";
	public static final String END_ENTITY_ALIAS = "end";

	/**
	 * Generate a X500PrivateCredential for the root entity.
	 */
	public static X500PrivateCredential createRootCredential() throws Exception {
		KeyPair rootPair = generateRSAKeyPair();
		X509Certificate rootCert = generateRootCert(rootPair);
		return new X500PrivateCredential(rootCert, rootPair.getPrivate(), ROOT_ALIAS);
	}

	/**
	 * Create a random 1024 bit RSA key pair
	 */
	public static KeyPair generateRSAKeyPair() throws Exception {
		KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
		kpGen.initialize(1024, new SecureRandom());
		return kpGen.generateKeyPair();
	}

	/**
	 * Generate a X509 Certificate with a given key pair
	 */
	public static X509Certificate generateRootCert(KeyPair pair) throws Exception {
		X509v1CertificateBuilder builder = new JcaX509v1CertificateBuilder(new X500Name("CN=Test CA Certificate"),
				new BigInteger(64, new SecureRandom()), new Date(),
				new Date(System.currentTimeMillis() + VALIDITY_PERIOD_MS), new X500Name("CN=Test CA Certificate"),
				pair.getPublic());
		ContentSigner signer = new JcaContentSignerBuilder("SHA1WithRSAEncryption").build(pair.getPrivate());
		return new JcaX509CertificateConverter().getCertificate(builder.build(signer));
	}

	/**
	 * Generate a X500PrivateCredential for the intermediate entity.
	 */
	public static X500PrivateCredential createIntermediateCredential(PrivateKey caKey, X509Certificate caCert)
			throws Exception {
		KeyPair interPair = generateRSAKeyPair();
		X509Certificate interCert = generateIntermediateCert(interPair.getPublic(), caKey, caCert);
		return new X500PrivateCredential(interCert, interPair.getPrivate(), INTERMEDIATE_ALIAS);
	}
	/**
	 * Generate an intermediate X509 Certificate with a given CA cert, public and private CA key
	 */
	public static X509Certificate generateIntermediateCert(PublicKey intKey, PrivateKey caKey, X509Certificate caCert)
			throws Exception {
		X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(caCert,
				new BigInteger(64, new SecureRandom()), new Date(),
				new Date(System.currentTimeMillis() + VALIDITY_PERIOD_MS),
				new X500Name("CN=Test Intermediate Certificate"), intKey);
		JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
		builder.addExtension(Extension.authorityKeyIdentifier, false,
				extensionUtils.createAuthorityKeyIdentifier(caCert));
		builder.addExtension(Extension.subjectKeyIdentifier, false, extensionUtils.createSubjectKeyIdentifier(intKey));
		builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(0));
		builder.addExtension(Extension.keyUsage, true,
				new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));
		ContentSigner signer = new JcaContentSignerBuilder("SHA1WithRSAEncryption").setProvider("BC").build(caKey);
		return new JcaX509CertificateConverter().getCertificate(builder.build(signer));
	}

	/**
	 * Generate a X500PrivateCredential for the end (final) entity.
	 */
	public static X500PrivateCredential createEndEntityCredential(PrivateKey caKey, X509Certificate caCert)
			throws Exception {
		KeyPair endPair = generateRSAKeyPair();
		X509Certificate endCert = generateEndEntityCert(endPair.getPublic(), caKey, caCert);
		return new X500PrivateCredential(endCert, endPair.getPrivate(), END_ENTITY_ALIAS);
	}

	public static X509Certificate generateEndEntityCert(PublicKey entityKey, PrivateKey caKey, X509Certificate caCert)
			throws Exception {
		X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(caCert,
				new BigInteger(64, new SecureRandom()), new Date(),
				new Date(System.currentTimeMillis() + VALIDITY_PERIOD_MS), new X500Name("CN=Test End Certificate"),
				entityKey);
		JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
		builder.addExtension(Extension.authorityKeyIdentifier, false,
				extensionUtils.createAuthorityKeyIdentifier(caCert));
		builder.addExtension(Extension.subjectKeyIdentifier, false,
				extensionUtils.createSubjectKeyIdentifier(entityKey));
		builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(0));
		builder.addExtension(Extension.keyUsage, true,
				new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
		ContentSigner signer = new JcaContentSignerBuilder("SHA1WithRSAEncryption").build(caKey);
		return new JcaX509CertificateConverter().getCertificate(builder.build(signer));
	}
	
	public static KeyStore createClientCredentialsP12(X500PrivateCredential rootCredential,
			String clientName, String clientPassword) throws Exception {

        X500PrivateCredential    interCredential = BCSSLUtils.createIntermediateCredential(rootCredential.getPrivateKey(), rootCredential.getCertificate());
        X500PrivateCredential    endCredential = BCSSLUtils.createEndEntityCredential(interCredential.getPrivateKey(), interCredential.getCertificate());
   
        // client credentials (always PKCS12)
        KeyStore keyStore = KeyStore.getInstance("PKCS12");  
        keyStore.load(null, null);  // create an empty keystore       
        keyStore.setKeyEntry(clientName, endCredential.getPrivateKey(), clientPassword.toCharArray(), 
                new Certificate[] { endCredential.getCertificate(), interCredential.getCertificate(), rootCredential.getCertificate() });
        
//        System.out.println("==== client credentials = " + config.getClientName() + ".p12");
//        keyStore.store(new FileOutputStream(config.getClientName() + "-1.p12"), config.getClientPasswordAry());
        
        return keyStore;
	}
	
	
	public static KeyStore createClientTrustStore(X500PrivateCredential rootCredential, String serverName, String storeType) throws Exception {
 
        KeyStore keyStore = KeyStore.getInstance(storeType);        
        keyStore.load(null, null);  // create an empty keystore        
        keyStore.setCertificateEntry(serverName, rootCredential.getCertificate());
        
//        System.out.println("==== truststore = " + config.getTrustStoreName() + config.getTrustStoreFileExt());
//        keyStore.store(new FileOutputStream(config.getTrustStoreName() + "-1" + config.getTrustStoreFileExt()), config.getTrustStorePasswordAry());
		
        return keyStore;
	}
	
	public static KeyStore createServerKeyStore(X500PrivateCredential rootCredential, String serverName, String serverPassword, String storeType) throws Exception {
        // server credentials
        KeyStore keyStore = KeyStore.getInstance(storeType);        
        keyStore.load(null, null);  // create an empty keystore        
        keyStore.setKeyEntry(serverName, rootCredential.getPrivateKey(), serverPassword.toCharArray(),
                new Certificate[] { rootCredential.getCertificate() });
        
//        System.out.println("=== keystore = " + config.getServerName() + config.getKeyStoreFileExt());
//        keyStore.store(new FileOutputStream(config.getServerName() + config.getKeyStoreFileExt()), config.getServerPasswordAry());
        
        return keyStore;
	}
}
