package org.unibl.etf.sni.backend.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;

import java.security.cert.Certificate;
import java.time.LocalDate;
import java.util.List;

import static org.unibl.etf.sni.backend.certificate.MessageHasher.createDigitalSignature;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public LogModel insertNewLog(String message, Status status) {
        LogModel model = new LogModel();
        model.setInfo(message);
        model.setStatus(status);
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);
        model.setDate(sqlDate);
        return logRepository.save(model);
    }

}
