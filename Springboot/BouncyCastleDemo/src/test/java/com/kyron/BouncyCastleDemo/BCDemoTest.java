package com.kyron.BouncyCastleDemo;

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

import chapter10.BC_SSLUtils;

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

		KeyPair rootKeyPair = BC_SSLUtils.generateRSAKeyPair();
		X509Certificate rootCert = BC_SSLUtils.generateRootCert(rootKeyPair);

		X500PrivateCredential intermediateCredential = BC_SSLUtils.createIntermediateCredential(rootKeyPair.getPrivate(), rootCert);

		X500PrivateCredential endEntityCredential = BC_SSLUtils.createEndEntityCredential(
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
		writer.write("End ---------------- \n");
		writer.writeObject(endEntityCredential.getCertificate());
		writer.close();
		System.out.println("test create root and entity credential PEM done");
	}
	
	@Test
	void createKeyStoreTrustStoreWithConfig() throws Exception {

        X500PrivateCredential    rootCredential = BC_SSLUtils.createRootCredential();
        X500PrivateCredential    interCredential = BC_SSLUtils.createIntermediateCredential(rootCredential.getPrivateKey(), rootCredential.getCertificate());
        X500PrivateCredential    endCredential = BC_SSLUtils.createEndEntityCredential(interCredential.getPrivateKey(), interCredential.getCertificate());
        
        // client credentials (always PKCS12)
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//        KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");   
        keyStore.load(null, null);  // create an empty keystore       
        keyStore.setKeyEntry(config.getClientName(), endCredential.getPrivateKey(), config.getClientPasswordAry(), 
                new Certificate[] { endCredential.getCertificate(), interCredential.getCertificate(), rootCredential.getCertificate() });
        
        System.out.println("==== client credentials = " + config.getClientName() + "-1.p12");
        keyStore.store(new FileOutputStream(config.getClientName() + "-1.p12"), config.getClientPasswordAry());
        
        // trust store for client
        keyStore = KeyStore.getInstance(config.getTrustStoreType());        
        keyStore.load(null, null);  // create an empty keystore        
        keyStore.setCertificateEntry(config.getServerName(), rootCredential.getCertificate());
        
        // NOTE: you cannot write to src/test/resource
        System.out.println("==== truststore = " + config.getTrustStoreName() + "-1" + config.getTrustStoreFileExt());
        keyStore.store(new FileOutputStream(config.getTrustStoreName() + "-1" + config.getTrustStoreFileExt()), config.getTrustStorePasswordAry());
        
        // server credentials
        keyStore = KeyStore.getInstance(config.getKeyStoreType());        
        keyStore.load(null, null);  // create an empty keystore        
        keyStore.setKeyEntry(config.getServerName(), rootCredential.getPrivateKey(), config.getServerPasswordAry(),
                new Certificate[] { rootCredential.getCertificate() });
        
        System.out.println("=== keystore = " + config.getServerName() + "-1" + config.getKeyStoreFileExt());
        keyStore.store(new FileOutputStream(config.getServerName() + "-1" + config.getKeyStoreFileExt()), config.getServerPasswordAry());
        
	}

}
