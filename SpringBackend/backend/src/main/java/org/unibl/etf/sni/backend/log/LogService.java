package org.unibl.etf.sni.backend.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public List<LogModel> getAllLogs() {
        return logRepository.findAll();
    }

    public LogModel insertNewLog(LogModel logModel) {
        return logRepository.save(logModel);
    }

}
