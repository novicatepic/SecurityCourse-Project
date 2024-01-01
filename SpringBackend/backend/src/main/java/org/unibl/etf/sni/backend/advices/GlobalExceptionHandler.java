package org.unibl.etf.sni.backend.advices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.unibl.etf.sni.backend.exception.InvalidRoleException;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.log.LogModel;
import org.unibl.etf.sni.backend.log.LogService;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private LogService logService;

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound(NotFoundException err) {
        logger.error("Resource not found!");
        logService.insertNewLog(new LogModel(err.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleIOException(IOException err) {
        logger.error("Error: " + err.getMessage());
        logService.insertNewLog(new LogModel(err.getMessage()));
    }

    @ExceptionHandler(InvalidRoleException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleInvalidRoleException(InvalidRoleException err) {
        logger.error("No permissions for that action!");
        logService.insertNewLog(new LogModel(err.getMessage()));
    }
}
