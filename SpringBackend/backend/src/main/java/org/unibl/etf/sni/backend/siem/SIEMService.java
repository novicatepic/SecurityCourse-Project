package org.unibl.etf.sni.backend.siem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.log.LogModel;
import org.unibl.etf.sni.backend.log.LogRepository;
import org.unibl.etf.sni.backend.log.Status;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;

import java.security.cert.Certificate;
import java.sql.Date;

import static org.unibl.etf.sni.backend.certificate.MessageHasher.createDigitalSignature;

@Service
public class SIEMService {

    Certificate wafCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.wafAlias);
    Certificate siemCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.siemAlias);

    @Autowired
    private LogRepository repository;

    public ProtocolMessages checkDigitallySignedMessage(String messageStr, byte[] message, String route, Status status) throws Exception  {
        boolean validity = Validator.checkMessageValidity(messageStr, message, wafCertificate);
        Status toWriteStatus;
        String toWriteMessage;
        boolean correct = true;
        if(!validity && status==Status.CHANGED) {
            toWriteStatus = Status.DOUBLE_CHANGED;
            toWriteMessage = "Found change id route " + route + " in protocol communication!";
            correct = false;
            //return ProtocolMessages.NOT_OK;
        } else if(!validity && status == Status.CORRECT) {
            toWriteStatus = Status.CHANGED;
            toWriteMessage = "Found change for route " + route + " in protocol communication!";
            correct = false;
            //return ProtocolMessages.NOT_OK;
        } else if(validity && status == Status.CORRECT) {
            toWriteStatus = Status.CORRECT;
            toWriteMessage = "Route " + route + " not changed in protocol communication!";
            correct = true;
        }
        else {
            toWriteStatus = Status.CHANGED;
            toWriteMessage = "Found change for route " + route + " in protocol communication!";
            correct = false;
        }


        logAction(toWriteMessage, toWriteStatus);

        if(correct) {
            return ProtocolMessages.OK;
        }
        return ProtocolMessages.NOT_OK;

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
