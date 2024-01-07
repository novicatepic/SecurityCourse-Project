package org.unibl.etf.sni.backend.permission;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.MessageHasher;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.jwtconfig.TokenExtractor;
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
    public ResponseEntity<UserRoomPermissionEntity> createPermissions(@RequestBody UserRoomPermissionEntity entity,
                                                                      HttpServletRequest request) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException {

        tokenExtractor(request);

        byte[] response = wafService.authorizePermissionModification(entity.getUserId(), request.getRequestURI(), MessageHasher.createDigitalSignature(entity.getUserId().toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.insert(entity), HttpStatus.OK);
    }

    @GetMapping("/set/{userId}")
    public ResponseEntity<List<UserRoomPermissionEntity>> getSetPermissions(@PathVariable("userId") Integer userId,
                                                                            HttpServletRequest request) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException {

        tokenExtractor(request);

        if(!wafService.checkNumberLength(userId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }

        byte[] response = wafService.authorizePermissionModification(userId, request.getRequestURI(), MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }


        return new ResponseEntity<>(service.getSetPermissions(userId), HttpStatus.OK);
    }

    @GetMapping("/{roomId}/{userId}")
    public ResponseEntity<UserRoomPermissionEntity> getPermissionsForProgram(
            @PathVariable("userId") Integer userId,
            @PathVariable("roomId") Integer roomId,
            HttpServletRequest request) throws NotFoundException {

        tokenExtractor(request);

        if(!wafService.checkNumberLength(userId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }
        if(!wafService.checkNumberLength(roomId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(service.getPermissionForRoomAndUser(userId, roomId), HttpStatus.OK);
    }

    @GetMapping("/unset/{userId}")
    public ResponseEntity<List<RoomModel>> getUnsetPermissions(@PathVariable("userId") Integer userId,
                                                               HttpServletRequest request) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException {

        tokenExtractor(request);

        if(!wafService.checkNumberLength(userId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }

        byte[] response = wafService.authorizePermissionModification(userId, request.getRequestURI(), MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.getUnsetPermissions(userId), HttpStatus.OK);
    }

    private void tokenExtractor(HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        wafService.setToken(token);
    }

}
