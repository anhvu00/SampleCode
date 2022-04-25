package com.kyron.BouncyCastleDemo;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

// To create a certificate chain we need the issuers certificate and private key. Keep these together to pass around
final public class GeneratedCert {
    public final PrivateKey privateKey;
    public final X509Certificate certificate;

    public GeneratedCert(PrivateKey privateKey, X509Certificate certificate) {
        this.privateKey = privateKey;
        this.certificate = certificate;
    }
}