package org.unibl.etf.sni.backend.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.room.RoomModel;
import org.unibl.etf.sni.backend.waf.WAFService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/permissions")
public class UserRoomPermissionController {

    @Autowired
    private UserRoomPermissionService service;

    @Autowired
    private WAFService wafService;

    @PostMapping
    public ResponseEntity<UserRoomPermissionEntity> createPermissions(@RequestBody UserRoomPermissionEntity entity) {
        if(!wafService.authorizePermissionRequests()) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.authorizePermissionModification(entity.getUserId())) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.insert(entity), HttpStatus.OK);
    }

    @GetMapping("/set/{userId}")
    public ResponseEntity<List<UserRoomPermissionEntity>> getSetPermissions(@PathVariable("userId") Integer userId) {

        if(!wafService.authorizePermissionRequests()) {
            return BadEntity.returnForbidden();
        }

        //hashing problems
        /*if(!wafService.checkNumberLength(userId)) {
            return BadEntity.returnBadRequst();
        }*/

        //needs-testing
        if(!wafService.authorizePermissionModification(userId)) {
            return BadEntity.returnForbidden();
        }


        return new ResponseEntity<>(service.getSetPermissions(userId), HttpStatus.OK);
    }

    @GetMapping("/{roomId}/{userId}")
    public ResponseEntity<UserRoomPermissionEntity> getPermissionsForProgram(
            @PathVariable("userId") Integer userId,
            @PathVariable("roomId") Integer roomId) throws NotFoundException {

        /*if(!wafService.authorizePermissionRequests()) {
            return ForbiddenEntity.returnForbidden();
        }

        if(!wafService.authorizePermissionModification(userId)) {
            return ForbiddenEntity.returnForbidden();
        }*/

        //hashing problems
        /*if(!wafService.checkNumberLength(roomId)) {
            return BadEntity.returnBadRequst();
        }
        if(!wafService.checkNumberLength(userId)) {
            return BadEntity.returnBadRequst();
        }*/
        return new ResponseEntity<>(service.getPermissionForRoomAndUser(userId, roomId), HttpStatus.OK);
    }

    @GetMapping("/unset/{userId}")
    public ResponseEntity<List<RoomModel>> getUnsetPermissions(@PathVariable("userId") Integer userId) {

        if(!wafService.authorizePermissionRequests()) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.authorizePermissionModification(userId)) {
            return BadEntity.returnForbidden();
        }

        //hashing problems
        /*if(!wafService.checkNumberLength(userId)) {
            return BadEntity.returnBadRequst();
        }*/

        return new ResponseEntity<>(service.getUnsetPermissions(userId), HttpStatus.OK);
    }

}
