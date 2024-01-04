package org.unibl.etf.sni.backend.exception;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {}

    public AccessDeniedException(String message) {
        super(message);
    }

}
