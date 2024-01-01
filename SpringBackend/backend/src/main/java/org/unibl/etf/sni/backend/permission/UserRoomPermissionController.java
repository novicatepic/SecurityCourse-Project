package org.unibl.etf.sni.backend.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/permissions")
public class UserRoomPermissionController {

    @Autowired
    private UserRoomPermissionService service;

    @PostMapping
    public ResponseEntity<UserRoomPermissionEntity> createPermissions(@RequestBody UserRoomPermissionEntity entity) {
        return new ResponseEntity<>(service.insert(entity), HttpStatus.OK);
    }

}
