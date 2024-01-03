package org.unibl.etf.sni.backend.certificate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageHasher {

    public static String hashMessage(String message) throws NoSuchAlgorithmException {
        // Get an instance of the SHA-256 algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Convert the message to bytes and hash it
        byte[] hashedBytes = digest.digest(message.getBytes(StandardCharsets.UTF_8));

        // Convert the hashed bytes to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static byte[] createDigitalSignature(String message, String alias) throws Exception {
        String hashedMessage = MessageHasher.hashMessage(message);
        return MessageRSAManipulator.encryptMessage(hashedMessage,
                CertificateAliasResolver.getPrivateKey(alias));
    }

}
