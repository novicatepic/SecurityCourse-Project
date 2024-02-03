package org.unibl.etf.sni.backend.advices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.unibl.etf.sni.backend.exception.InvalidRoleException;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.exception.PasswordTooShortException;
import org.unibl.etf.sni.backend.exception.RegistrationNotAllowed;
import org.unibl.etf.sni.backend.log.LogModel;
import org.unibl.etf.sni.backend.log.LogService;
import org.unibl.etf.sni.backend.log.Status;

import java.io.IOException;

//class for handling and logging exceptions
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private LogService logService;

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound(NotFoundException err) {
        logService.insertNewLog(err.getMessage(), Status.ERROR);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleIOException(IOException err) {
        logger.error("Error: " + err.getMessage());
        logService.insertNewLog(err.getMessage(), Status.ERROR);
    }

    @ExceptionHandler(InvalidRoleException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleInvalidRoleException(InvalidRoleException err) {
        logger.error("No permissions for that action!");
        logService.insertNewLog(err.getMessage(), Status.ERROR);
    }

    @ExceptionHandler(RegistrationNotAllowed.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleRegistrationNotAllowed(RegistrationNotAllowed err) {
        logger.error("Registration not allowed!");
        logService.insertNewLog("Registration not allowed!", Status.ERROR);
    }

    @ExceptionHandler(PasswordTooShortException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlePasswordTooShortException(PasswordTooShortException err) {
        logger.error("Password to short!");
        logService.insertNewLog("Password to short!", Status.ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidationExceptions(
            MethodArgumentNotValidException ex) {
            ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            logger.error("Wrong input for field with name " + fieldName + " with message " + errorMessage);
            logService.insertNewLog("Wrong input for field with name " + fieldName + " with message " + errorMessage, Status.ERROR);
        });
    }
}
