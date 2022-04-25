package com.kyron.BouncyCastleDemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500PrivateCredential;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.ResourceUtils;

import chapter10.BCSSLUtils;

@SpringBootTest(classes = { DemoSSLConfig.class })
@TestPropertySource("classpath:application.properties")
class BCDemoTest {
//    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

	@Autowired
	private DemoSSLConfig config;

	@BeforeEach
	void init() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
	}

	// todo: add tests to create cert, keystore, truststore, then mock server,
	// client

	@Test
	void createRootAndEntityCredential() throws Exception {

		KeyPair rootKeyPair = BCSSLUtils.generateRSAKeyPair();
		X509Certificate rootCert = BCSSLUtils.generateRootCert(rootKeyPair);

		X500PrivateCredential intermediateCredential = BCSSLUtils.createIntermediateCredential(rootKeyPair.getPrivate(), rootCert);

		X500PrivateCredential endEntityCredential = BCSSLUtils.createEndEntityCredential(
				intermediateCredential.getPrivateKey(), intermediateCredential.getCertificate());

		System.out.println("ROOT ---------------------  ");
		JcaPEMWriter writer = new JcaPEMWriter(new PrintWriter(System.out));
		writer.writeObject(rootCert);
		writer.flush();
		writer.write("Intermediate ---------------- \n");
		writer.writeObject(intermediateCredential.getCertificate());
		writer.flush();
		writer.write("\n");
		writer.flush();
		writer.writeObject(endEntityCredential.getCertificate());
		writer.close();
		System.out.println("test create root and entity credential PEM done");
	}
	
	// from CreateKeystores.java
//	@Test
	void createKeyStoreTrustStore() throws Exception {

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
        
        // NOTE: you cannot write to src/test/resource, read only
        System.out.println("==== truststore = " + BCSSLUtils.TRUST_STORE_NAME + ".jks");
        keyStore.store(new FileOutputStream(BCSSLUtils.TRUST_STORE_NAME + ".jks"), BCSSLUtils.TRUST_STORE_PASSWORD);
        
        // server credentials
        keyStore = KeyStore.getInstance("JKS");
        
        keyStore.load(null, null);
        
        keyStore.setKeyEntry(BCSSLUtils.SERVER_NAME, rootCredential.getPrivateKey(), BCSSLUtils.SERVER_PASSWORD,
                new Certificate[] { rootCredential.getCertificate() });
        
        System.out.println("=== keystore = " + BCSSLUtils.SERVER_NAME + ".jks");
        keyStore.store(new FileOutputStream(BCSSLUtils.SERVER_NAME + ".jks"), BCSSLUtils.SERVER_PASSWORD);
	}
	
	@Test
	void createKeyStoreTrustStoreWithConfig() throws Exception {

        X500PrivateCredential    rootCredential = BCSSLUtils.createRootCredential();
        X500PrivateCredential    interCredential = BCSSLUtils.createIntermediateCredential(rootCredential.getPrivateKey(), rootCredential.getCertificate());
        X500PrivateCredential    endCredential = BCSSLUtils.createEndEntityCredential(interCredential.getPrivateKey(), interCredential.getCertificate());
        
        // client credentials (always PKCS12)
        KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");        
        keyStore.load(null, null);  // create an empty keystore       
        keyStore.setKeyEntry(config.getClientName(), endCredential.getPrivateKey(), config.getClientPasswordAry(), 
                new Certificate[] { endCredential.getCertificate(), interCredential.getCertificate(), rootCredential.getCertificate() });
        
        System.out.println("==== client credentials = " + config.getClientName() + ".p12");
        keyStore.store(new FileOutputStream(config.getClientName() + ".p12"), config.getClientPasswordAry());
        
        // trust store for client
        keyStore = KeyStore.getInstance(config.getTrustStoreType());        
        keyStore.load(null, null);  // create an empty keystore        
        keyStore.setCertificateEntry(config.getServerName(), rootCredential.getCertificate());
        
        // NOTE: you cannot write to src/test/resource
        System.out.println("==== truststore = " + config.getTrustStoreName() + config.getTrustStoreFileExt());
        keyStore.store(new FileOutputStream(config.getTrustStoreName() + config.getTrustStoreFileExt()), config.getTrustStorePasswordAry());
        
        // server credentials
        keyStore = KeyStore.getInstance(config.getKeyStoreType());        
        keyStore.load(null, null);  // create an empty keystore        
        keyStore.setKeyEntry(config.getServerName(), rootCredential.getPrivateKey(), config.getServerPasswordAry(),
                new Certificate[] { rootCredential.getCertificate() });
        
        System.out.println("=== keystore = " + config.getServerName() + config.getKeyStoreFileExt());
        keyStore.store(new FileOutputStream(config.getServerName() + config.getKeyStoreFileExt()), config.getServerPasswordAry());
        
	}


}
