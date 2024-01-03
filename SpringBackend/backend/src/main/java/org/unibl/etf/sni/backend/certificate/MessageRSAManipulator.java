package org.unibl.etf.sni.backend.certificate;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

public class MessageRSAManipulator {

    public static byte[] encryptMessage(String message, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return bytes;

    }

    public static byte[] decryptMessage(byte[] encryptedData, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(encryptedData);
    }
}
