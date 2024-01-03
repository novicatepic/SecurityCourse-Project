package org.unibl.etf.sni.backend.certificate;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class CertificateAliasResolver {

    public static final String acAlias = "AccessController";
    public static final String wafAlias = "WAF";
    public static final String siemAlias = "SIEM";
    private static CertificateManager certificateManager = new CertificateManager();


    public static Certificate getCertificateByAlias(String alias) {
        return certificateManager.getCertificate(alias);
    }

    public static PrivateKey getPrivateKey(String alias) throws Exception {
        KeyStore keyStore = certificateManager.getKeyStore();
        return (PrivateKey) keyStore.getKey(alias, "keystore".toCharArray());
    }

}
