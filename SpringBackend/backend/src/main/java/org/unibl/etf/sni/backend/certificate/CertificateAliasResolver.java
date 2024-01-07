package org.unibl.etf.sni.backend.certificate;

import org.springframework.beans.factory.annotation.Value;

import java.security.*;
import java.security.cert.Certificate;

public class CertificateAliasResolver {

    public static final String acAlias = "AccessController";
    public static final String wafAlias = "WAF";
    public static final String siemAlias = "SIEM";

    /*@Value("${spring.certificate.alias.ac}")
    public static String acAlias;

    @Value("${spring.certificate.alias.waf}")
    public static String wafAlias;

    @Value("${spring.certificate.alias.siem}")
    public static String siemAlias;*/

    private static CertificateManager certificateManager = new CertificateManager();


    public static Certificate getCertificateByAlias(String alias) {
        if(!certificateManager.checkCertificateValidity(alias)) {
            certificateManager.renewCertificate(alias);
        }
        return certificateManager.getCertificate(alias);
    }

    public static PrivateKey getPrivateKey(String alias) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        KeyStore keyStore = certificateManager.getKeyStore();
        return (PrivateKey) keyStore.getKey(alias, "keystore".toCharArray());
    }

}
