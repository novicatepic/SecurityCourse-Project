package org.unibl.etf.sni.backend.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.MessageHasher;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.jsonconverter.JSONConverter;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;
import org.unibl.etf.sni.backend.room.RoomModel;
import org.unibl.etf.sni.backend.waf.WAFService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

//@CrossOrigin("*")
@CrossOrigin(origins = "https://localhost:4200")
@RestController
@RequestMapping("/permissions")
public class UserRoomPermissionController {

    @Autowired
    private UserRoomPermissionService service;

    @Autowired
    private WAFService wafService;

    @PostMapping
    public ResponseEntity<UserRoomPermissionEntity> createPermissions(@RequestBody UserRoomPermissionEntity entity) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException {

        byte[] response = wafService.authorizePermissionModification(entity.getUserId(), "/permissions/update-role", MessageHasher.createDigitalSignature(entity.getUserId().toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.insert(entity), HttpStatus.OK);
    }

    @GetMapping("/set/{userId}")
    public ResponseEntity<List<UserRoomPermissionEntity>> getSetPermissions(@PathVariable("userId") Integer userId) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException {

        if(!wafService.checkNumberLength(userId, "/permissions/set/"+userId)) {
            return BadEntity.returnBadRequst();
        }

        byte[] response = wafService.authorizePermissionModification(userId, "/permissions/set/"+userId, MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }


        return new ResponseEntity<>(service.getSetPermissions(userId), HttpStatus.OK);
    }

    @GetMapping("/{roomId}/{userId}")
    public ResponseEntity<UserRoomPermissionEntity> getPermissionsForProgram(
            @PathVariable("userId") Integer userId,
            @PathVariable("roomId") Integer roomId) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException, NotFoundException {

        if(!wafService.checkNumberLength(userId, "/permissions/"+roomId+"/"+userId)) {
            return BadEntity.returnBadRequst();
        }
        if(!wafService.checkNumberLength(roomId, "/permissions/"+roomId+"/"+userId)) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(service.getPermissionForRoomAndUser(userId, roomId), HttpStatus.OK);
    }

    @GetMapping("/unset/{userId}")
    public ResponseEntity<List<RoomModel>> getUnsetPermissions(@PathVariable("userId") Integer userId) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException {

        if(!wafService.checkNumberLength(userId, "/permissions/unset/"+userId)) {
            return BadEntity.returnBadRequst();
        }

        byte[] response = wafService.authorizePermissionModification(userId, "/permissions/unset/"+userId, MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.getUnsetPermissions(userId), HttpStatus.OK);
    }

}
