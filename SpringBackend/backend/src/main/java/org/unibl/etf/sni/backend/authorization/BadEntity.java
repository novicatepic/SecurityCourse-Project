package org.unibl.etf.sni.backend.authorization;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BadEntity {

    public static ResponseEntity returnForbidden() {
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity returnBadRequst() {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
