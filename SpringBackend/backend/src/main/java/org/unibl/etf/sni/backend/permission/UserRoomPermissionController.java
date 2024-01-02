package org.unibl.etf.sni.backend.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.room.RoomModel;

import java.util.List;

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

    @GetMapping("/set/{userId}")
    public ResponseEntity<List<UserRoomPermissionEntity>> getSetPermissions(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<>(service.getSetPermissions(userId), HttpStatus.OK);
    }

    @GetMapping("/unset/{userId}")
    public ResponseEntity<List<RoomModel>> getUnsetPermissions(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<>(service.getUnsetPermissions(userId), HttpStatus.OK);
    }

}
