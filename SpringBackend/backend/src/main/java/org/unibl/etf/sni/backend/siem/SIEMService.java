package org.unibl.etf.sni.backend.siem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.log.LogModel;
import org.unibl.etf.sni.backend.log.LogRepository;

@Service
public class SIEMService {

    @Autowired
    private LogRepository repository;

    public LogModel logAction(String message) {
        LogModel logModel = new LogModel();
        logModel.setInfo(message);
        return repository.save(logModel);
    }

}
