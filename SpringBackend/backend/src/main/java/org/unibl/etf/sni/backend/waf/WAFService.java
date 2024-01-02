package org.unibl.etf.sni.backend.waf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.log.LogModel;
import org.unibl.etf.sni.backend.log.LogRepository;
import org.unibl.etf.sni.backend.siem.SIEMService;

@Service
public class WAFService {

    @Autowired
    private SIEMService service;

    public Boolean checkStringValidity(String request) {

        if(!checkMySQLInjection(request)) {
            return false;
        }

        if(!checkXSSInjection(request)) {
            return false;
        }

        return false;
    }

    private Boolean checkMySQLInjection(String request) {

        for (String keyword : SQLProblemKeywords.returnSQLKeywords()) {
            if (request.toUpperCase().contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    private Boolean checkXSSInjection(String request) {

        for (String keyword : XSSProblemKeywords.returnXSSPatterns()) {
            if (request.toLowerCase().contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    public LogModel logAction(String message) {
        return service.logAction(message);
    }

}
