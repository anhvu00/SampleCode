package com.kyron.BouncyCastleDemo;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500PrivateCredential;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = { DemoSSLConfig.class })
@TestPropertySource("classpath:application.properties")
class BCSSLUtilsTest {
	
	@Autowired
	DemoSSLConfig config;

	@BeforeEach
	void init() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
	}
	/*
	 * Test the new BCSSLUtils
	 */

	@Test
	void createRootIntermediateAndEndCredentialPEM() throws Exception {

		KeyPair rootKeyPair = BCSSLUtils.generateRSAKeyPair();
		X509Certificate rootCert = BCSSLUtils.generateRootCert(rootKeyPair);
		X500PrivateCredential intermediateCredential = BCSSLUtils.createIntermediateCredential(rootKeyPair.getPrivate(),
				rootCert);
		X500PrivateCredential endEntityCredential = BCSSLUtils.createEndEntityCredential(
				intermediateCredential.getPrivateKey(), intermediateCredential.getCertificate());

		System.out.println("ROOT CERT  ");
		JcaPEMWriter writer = new JcaPEMWriter(new PrintWriter(System.out));
		writer.writeObject(rootCert);
		writer.flush();
		writer.write("Intermediate Cert \n");
		writer.writeObject(intermediateCredential.getCertificate());
		writer.flush();
		writer.write("\n");
		writer.flush();
		writer.write("End Cert \n");
		writer.writeObject(endEntityCredential.getCertificate());
		writer.close();
		System.out.println("test create root, intermediate, and end credential PEM done");
	}

	@Test
	void createAll() throws Exception {
		String path = "./src/main/resources/stores/";
		
		X500PrivateCredential rootCredential = BCSSLUtils.createRootCredential();

		KeyStore keyStore = BCSSLUtils.createClientCredentialsP12(rootCredential, config.getClientName(), config.getClientPassword());
		System.out.println("==== client credentials = " + path + config.getClientName() + ".p12");
		keyStore.store(new FileOutputStream(path + config.getClientName() + ".p12"), config.getClientPasswordAry());

		keyStore = BCSSLUtils.createClientTrustStore(rootCredential, config.getTrustStoreName(), config.getTrustStoreType());

		System.out.println("==== truststore = " + path + config.getTrustStoreName() + config.getTrustStoreFileExt());
		keyStore.store(new FileOutputStream(path + config.getTrustStoreName() + config.getTrustStoreFileExt()),
				config.getTrustStorePasswordAry());


		keyStore = BCSSLUtils.createServerKeyStore(rootCredential, config.getServerName(), config.getServerPassword(), config.getKeyStoreType());
		System.out.println("=== keystore = " + path + config.getServerName() + config.getKeyStoreFileExt());
		keyStore.store(new FileOutputStream(path + config.getServerName() + config.getKeyStoreFileExt()),
				config.getServerPasswordAry());
		
	}
}