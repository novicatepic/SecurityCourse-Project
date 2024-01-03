package org.unibl.etf.sni.backend.certificate;

import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;

public class Validator {

    public static boolean checkMessageValidity(String messageStr, byte[] message, Certificate certificate) throws Exception {
        byte[] decryptedHash = MessageRSAManipulator.decryptMessage(message, certificate.getPublicKey());
        String hashedMessage = MessageHasher.hashMessage(messageStr);
        String stringDecrypted = new String(decryptedHash, StandardCharsets.UTF_8);
        return hashedMessage.equals(stringDecrypted);
    }

}
