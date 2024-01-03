package org.unibl.etf.sni.backend.certificate;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SymmetricKeyGenerator {

    private static final String ALGORITHM = "AES";

    /*private static SecretKey generateSymmetricKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    private static void writeKeyToFile(SecretKey secretKey, String filePath) throws IOException {
        byte[] keyBytes = secretKey.getEncoded();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(keyBytes);
        }
    }*/

    public static SecretKey readKeyFromFile() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get("./symmetric_key.key"));
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static byte[] encryptMessage(String message) throws Exception {
        SecretKey secretKey = readKeyFromFile();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(message.getBytes());
    }

    public static String decryptMessage(byte[] encryptedMessage) throws Exception {
        SecretKey secretKey = readKeyFromFile();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedMessage);
        return new String(decryptedBytes);
    }
}
