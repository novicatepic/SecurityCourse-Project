package org.unibl.etf.sni.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.unibl.etf.sni.backend.certificate.*;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.cert.Certificate;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) throws Exception {
		/*Certificate accessControllerCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.acAlias);
		String mssg = "abc";
		String hashedMessage = MessageHasher.hashMessage(mssg);
		byte[] encryptedMessage = MessageRSAManipulator.signMessage(hashedMessage,
				CertificateAliasResolver.getPrivateKey(CertificateAliasResolver.acAlias));


		byte[] decryptedMessage = decryptMessage(encryptedMessage, accessControllerCertificate.getPublicKey());

		System.out.println("HASH " + hashedMessage);
		String result = new String(decryptedMessage, StandardCharsets.UTF_8);
		System.out.println("DECR " + result);

		System.out.println(hashedMessage.equals(result));*/

		SpringApplication.run(BackendApplication.class, args);
	}



}
