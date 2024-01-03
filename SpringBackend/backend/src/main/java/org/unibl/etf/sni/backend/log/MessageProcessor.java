package org.unibl.etf.sni.backend.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;
import org.unibl.etf.sni.backend.waf.WAFService;

import static org.unibl.etf.sni.backend.certificate.MessageHasher.createDigitalSignature;

public class MessageProcessor {


    public static Boolean processMessage(String message, String route, WAFService wafService) throws Exception {

        byte[] mssg = createDigitalSignature(message, CertificateAliasResolver.acAlias);
        if(!wafService.checkDigitallySignedMessage(message, route, mssg).equals(ProtocolMessages.OK)) {
            return false;
        }
        return true;
    }

}
