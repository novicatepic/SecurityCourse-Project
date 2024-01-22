package org.unibl.etf.sni.backend.certificate;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

@Component
public class CertificateManager {
    private static final String KEYSTORE_PASSWORD = "keystore";
    //private static final String KEYSTORE_FILE_PATH = "C:\\Users\\User\\Desktop\\SecurityCourse-Project\\SpringBackend\\backend\\keystore.jks";
    private static final String KEYSTORE_FILE_PATH = "."+ File.separator+"keystore.jks";
    private static final String KEYSTORE_TYPE = "JKS";
    private static final String MY_KEY_ALIAS = "myKeyAlias";

    private KeyStore keyStore;

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public CertificateManager() {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            FileInputStream fis = new FileInputStream(KEYSTORE_FILE_PATH);
            keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
            initializeKeyStore();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing keystore", e);
        }
    }

    private void initializeKeyStore() {
        try {
            if (!keyStore.containsAlias(MY_KEY_ALIAS)) {
                KeyPair keyPair = generateKeyPair();
                X509Certificate certificate = generateSelfSignedCertificate(keyPair);
                keyStore.setKeyEntry(MY_KEY_ALIAS, keyPair.getPrivate(), KEYSTORE_PASSWORD.toCharArray(), new Certificate[]{certificate});

                // Save the keystore to a file
                try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE_PATH)) {
                    keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
                }
            } /*else {
                System.out.println("Present");
            }*/
        } catch (Exception e) {
            throw new RuntimeException("Error initializing keystore", e);
        }
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        X500Name subjectName = new X500Name("CN=MyApp");

        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                subjectName,
                BigInteger.valueOf(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000), // Valid for 1 year
                subjectName,
                keyPair.getPublic()
        );

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());
        X509CertificateHolder certificateHolder = certBuilder.build(contentSigner);

        return new JcaX509CertificateConverter().getCertificate(certificateHolder);
    }


    public X509Certificate generateCertificate(String alias, int validityDays, String signerAlias) {
        try {
            //System.out.println("In");
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            X509Certificate certificate = generateX509Certificate(keyPair, alias, validityDays, signerAlias);

            // Do not set the key entry and store keystore here

            // Write the certificate to a file
            writeCertificateToFile(certificate, alias, keyPair);

            return certificate;
        } catch (Exception e) {
            throw new RuntimeException("Error generating certificate", e);
        }
    }

    public void writeCertificateToFile(X509Certificate certificate, String alias, KeyPair keyPair) {
        try (FileInputStream fis = new FileInputStream(KEYSTORE_FILE_PATH)) {

            // Load the existing keystore
            keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());

            // Set the key entry
            //System.out.println("Alias " + alias);
            keyStore.setKeyEntry(alias, keyPair.getPrivate(), KEYSTORE_PASSWORD.toCharArray(), new Certificate[]{certificate});

        } catch (Exception e) {
            throw new RuntimeException("Error loading keystore", e);
        }

        try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE_PATH)) {
            // Store the updated keystore back to the file
            keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
           // System.out.println("Written");
        } catch (Exception e) {
            throw new RuntimeException("Error writing certificate to file", e);
        }
    }
    private X509Certificate generateX509Certificate(KeyPair keyPair, String alias, int validityDays, String signerAlias)
            throws GeneralSecurityException, IOException, OperatorCreationException {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + validityDays * 24 * 60 * 60 * 1000L);

        X500Name issuerName = new X500NameBuilder(BCStyle.INSTANCE).addRDN(BCStyle.CN, signerAlias).build();
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

        if (signerAlias == null) {
            signerAlias = alias; // Self-sign the certificate
        }

        // Retrieve the signing key from the keystore
        PrivateKey signerPrivateKey = (PrivateKey) keyStore.getKey(signerAlias, KEYSTORE_PASSWORD.toCharArray());
        //X509Certificate signerCertificate = (X509Certificate) keyStore.getCertificate(signerAlias);

        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuerName,
                serialNumber,
                startDate,
                endDate,
                new X500Name("CN=" + alias),
                keyPair.getPublic()
        );

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(signerPrivateKey);

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
            // Remove the certificate from the keystore
            keyStore.deleteEntry(alias);

            // Save the updated keystore
            try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE_PATH)) {
                keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error disabling certificate", e);
        }

    }

    public void renewCertificate(String alias) {
        try {
            disableCertificate(alias);
            generateCertificate(alias, 365, MY_KEY_ALIAS);

            //System.out.println("Renewed certificate");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Certificate getCertificate(String alias) {
        try {
            return keyStore.getCertificate(alias);
        } catch (Exception e) {
            throw new RuntimeException("Error getting certificate from keystore", e);
        }
    }
}
