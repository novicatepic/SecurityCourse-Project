package org.unibl.etf.sni.backend.siem;

import org.bouncycastle.pqc.crypto.MessageSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.MessageHasher;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.log.LogModel;
import org.unibl.etf.sni.backend.log.LogRepository;
import org.unibl.etf.sni.backend.log.Status;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.sql.Date;

@Service
public class SIEMService {

    Certificate wafCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.wafAlias);
    Certificate siemCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.siemAlias);

    @Autowired
    private LogRepository repository;

    public byte[] checkDigitallySignedMessage(String messageStr, byte[] message, String route, Status status) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException {
        boolean validity = Validator.checkMessageValidity(messageStr, message, wafCertificate);
        /*Status toWriteStatus;
        String toWriteMessage;*/
        boolean correct = true;
        if(!validity && status == Status.CORRECT) {
            /*toWriteStatus = Status.CHANGED;
            toWriteMessage = "Found change for route " + route +
                    " with id " + messageStr + " in protocol communication between WAF and SIEM!";*/
            correct = false;
            //return ProtocolMessages.NOT_OK;
        } else if(validity && status == Status.CORRECT) {
            /*toWriteStatus = Status.CORRECT;
            toWriteMessage = "Route " + route + " with id " + messageStr + " not changed in protocol communication" +
                    " between WAF and SIEM!";*/
            correct = true;
            logAction("Correct try for " + messageStr + " on route " + route, Status.CORRECT);
        }
        else {
            /*toWriteStatus = Status.CHANGED;
            toWriteMessage = "Found change for route " + route + " with id " + messageStr + " in protocol communication" +
                    " between WAF and SIEM!";*/
            correct = false;
        }


        //logAction(toWriteMessage, toWriteStatus);

        if(correct) {
            return MessageHasher.createDigitalSignature(ProtocolMessages.OK.toString(), CertificateAliasResolver.siemAlias);
            //return ProtocolMessages.OK;
        }
        return MessageHasher.createDigitalSignature(ProtocolMessages.NOT_OK_SECOND_LEVEL.toString(), CertificateAliasResolver.siemAlias);

    }

    public ProtocolMessages writeDangerousAction(String messageStr, byte[] message) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        boolean validity = Validator.checkMessageValidity(messageStr, message, wafCertificate);
        if(messageStr.length() > 100) {
            messageStr = messageStr.substring(0, 100);
        }
        if(validity) {
            logAction(messageStr, Status.DANGER);
            return ProtocolMessages.OK;
        } else {
            logAction(messageStr, Status.CHANGED);
            return ProtocolMessages.NOT_OK_SECOND_LEVEL;
        }
    }

    private void logAction(String message, Status status) {
        LogModel logModel = new LogModel();
        logModel.setInfo(message);
        logModel.setStatus(status);
        Date currentDate = new Date(System.currentTimeMillis());
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
        logModel.setDate(sqlDate);
        repository.save(logModel);
    }

}
