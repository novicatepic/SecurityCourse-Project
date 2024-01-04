package org.unibl.etf.sni.backend.certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;

public class Validator {

    public static boolean checkMessageValidity(String messageStr, byte[] message, Certificate certificate) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        byte[] decryptedHash = MessageRSAManipulator.decryptMessage(message, certificate.getPublicKey());
        String hashedMessage = MessageHasher.hashMessage(messageStr);
        String stringDecrypted = new String(decryptedHash, StandardCharsets.UTF_8);
        /*System.out.println("Hashed " + hashedMessage);
        System.out.println("Decrypted " + stringDecrypted);*/
        return hashedMessage.equals(stringDecrypted);
    }

}
