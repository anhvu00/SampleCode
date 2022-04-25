package com.kyron.BouncyCastleDemo;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.context.annotation.Configuration;

/**
 * Inject application.properties values to local vars
 * @author anh
 *
 */
@Configuration
public class CertBuilder {
	
	public static void main(String[] args) throws Exception {
		
		// create a root cert
		String cnName = "root CA";
		String domainName = null;
		GeneratedCert cert = null;
		boolean isCA = true;
		GeneratedCert rootCACert = createCertificate(cnName, domainName, cert, isCA);

		// create a localhost cert, signed with rootCACert
		cnName = "intermediate CA";
		GeneratedCert interCACert = createCertificate(cnName, domainName, rootCACert, isCA);

		// create a localhost cert, signed with intermediate CA
		GeneratedCert localhostCert = createCertificate("localhost", "test.com", interCACert, false);
		
		// create a localhost cert, signed with intermediate CA		
		GeneratedCert otherHostCert = createCertificate("other.gamlor.info", "other.gamlor.info", interCACert, false);
		
		// create keystore
		String keystoreType = "PKCS12";
		char[] emptyPassword = new char[0];
		KeyStore keyStore = KeyStore.getInstance(keystoreType);
		
		// Key store expects a load first to initialize.
		keyStore.load(null, emptyPassword);
		
		// Store our domain certificate, with the private key and the cert chain
		// the alias is set to the same as cnName here
		keyStore.setKeyEntry("localhost", localhostCert.privateKey, emptyPassword,
				new X509Certificate[] { localhostCert.certificate, interCACert.certificate, rootCACert.certificate });
		keyStore.setKeyEntry("other.local.gamlor.info", otherHostCert.privateKey, emptyPassword,
				new X509Certificate[] { otherHostCert.certificate, interCACert.certificate, rootCACert.certificate });
		// Store to a file
		String keystoreName = "testKeystore.p12";
		try (FileOutputStream store = new FileOutputStream(keystoreName)) {
			keyStore.store(store, emptyPassword);
		}
	}

	/**
	 * @param cnName The CN={name} of the certificate. When the certificate is for a
	 *               domain it should be the domain name
	 * @param domain Nullable. The DNS domain for the certificate.
	 * @param issuer Issuer who signs this certificate. Null for a self-signed
	 *               certificate
	 * @param isCA   Can this certificate be used to sign other certificates
	 * @return Newly created certificate with its private key
	 */
	private static GeneratedCert createCertificate(String cnName, String domain, GeneratedCert issuer, boolean isCA)
			throws Exception {
		// Generate the key-pair with the official Java API's
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		KeyPair certKeyPair = keyGen.generateKeyPair();
		X500Name name = new X500Name("CN=" + cnName);
		// If you issue more than just test certificates, you might want a decent serial
		// number schema ^.^
		BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
		Instant validFrom = Instant.now();
		Instant validUntil = validFrom.plus(10 * 360, ChronoUnit.DAYS);

		// If there is no issuer, we self-sign our certificate.
		X500Name issuerName;
		PrivateKey issuerKey;
		if (issuer == null) {
			issuerName = name;
			issuerKey = certKeyPair.getPrivate();
		} else {
			issuerName = new X500Name(issuer.certificate.getSubjectDN().getName());
			issuerKey = issuer.privateKey;
		}

		// The cert builder to build up our certificate information
		JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(issuerName, serialNumber,
				Date.from(validFrom), Date.from(validUntil), name, certKeyPair.getPublic());

		// Make the cert to a Cert Authority to sign more certs when needed
		if (isCA) {
			builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(isCA));
		}
		// Modern browsers demand the DNS name entry
		if (domain != null) {
			builder.addExtension(Extension.subjectAlternativeName, false,
					new GeneralNames(new GeneralName(GeneralName.dNSName, domain)));
		}

		// Finally, sign the certificate:
		ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(issuerKey);
		X509CertificateHolder certHolder = builder.build(signer);
		X509Certificate cert = new JcaX509CertificateConverter().getCertificate(certHolder);

		return new GeneratedCert(certKeyPair.getPrivate(), cert);
	}
}
