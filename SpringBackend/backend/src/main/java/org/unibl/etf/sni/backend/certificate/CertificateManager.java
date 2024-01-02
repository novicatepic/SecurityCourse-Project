package org.unibl.etf.sni.backend.certificate;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x509.V1TBSCertificateGenerator;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

@Component
public class CertificateManager {

    private KeyStore keyStore;

    public CertificateManager() {
        try {
            // Load the default keystore. You may want to customize this based on your requirements.
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing keystore", e);
        }
    }

    public X509Certificate generateCertificate(String alias, int validityDays) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            X509Certificate certificate = generateX509Certificate(keyPair, alias, validityDays);

            keyStore.setKeyEntry(alias, keyPair.getPrivate(), null, new Certificate[]{certificate});

            return certificate;
        } catch (Exception e) {
            throw new RuntimeException("Error generating certificate", e);
        }
    }

    private X509Certificate generateX509Certificate(KeyPair keyPair, String alias, int validityDays)
            throws GeneralSecurityException, IOException, OperatorCreationException, CertificateException {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + validityDays * 24 * 60 * 60 * 1000L);

        X500Name issuerName = new X500NameBuilder().build();
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuerName,
                serialNumber,
                startDate,
                endDate,
                new X500Name("CN=" + alias),
                keyPair.getPublic()
        );

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());

        X509CertificateHolder certificateHolder = certBuilder.build(contentSigner);
        return new JcaX509CertificateConverter().getCertificate(certificateHolder);
    }

    public boolean checkCertificateValidity(String alias) {
        try {
            Certificate certificate = keyStore.getCertificate(alias);
            if (certificate instanceof X509Certificate) {
                X509Certificate x509Certificate = (X509Certificate) certificate;
                x509Certificate.checkValidity(); // This will throw an exception if the certificate is not valid
                return true;
            } else {
                throw new RuntimeException("Certificate not found or is not an X.509 certificate");
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void disableCertificate(String alias) {
        try {
            keyStore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            throw new RuntimeException("Error disabling certificate", e);
        }
    }

}
