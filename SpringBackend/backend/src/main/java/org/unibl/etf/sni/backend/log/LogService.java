package org.unibl.etf.sni.backend.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;

import java.security.cert.Certificate;
import java.util.List;

import static org.unibl.etf.sni.backend.certificate.MessageHasher.createDigitalSignature;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    Certificate wafCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.wafAlias);
    Certificate siemCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.siemAlias);

    public List<LogModel> getAllLogs() {
        return logRepository.findAll();
    }

    public LogModel insertNewLog(LogModel logModel) {
        return logRepository.save(logModel);
    }

    /*public LogModel insertWAFLog(String log, byte[] message) {
        if(Validator.checkMessageValidity(messageStr, message, accessControllerCertificate)) {
            String invalid = "Found changed message " + messageStr + " in protocol communication!";
            byte[] mssg = createDigitalSignature(invalid, CertificateAliasResolver.wafAlias);
            return ProtocolMessages.OK;
        }



        return ProtocolMessages.NOT_OK;
    }*/

}
