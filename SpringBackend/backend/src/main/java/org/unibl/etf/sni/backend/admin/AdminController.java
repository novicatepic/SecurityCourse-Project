package org.unibl.etf.sni.backend.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.MessageHasher;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.jwtconfig.TokenExtractor;
import org.unibl.etf.sni.backend.user.UserModel;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;
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
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService service;

    @Autowired
    private WAFService wafService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserModel> findUserById(@PathVariable("userId") Integer userId, HttpServletRequest request) throws NotFoundException {

        tokenExtractor(request);

        if(!wafService.checkNumberLength(userId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(service.getUserById(userId), HttpStatus.OK);
    }

    private void tokenExtractor(HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        wafService.setToken(token);
    }

    @GetMapping("/users-to-modify")
    public ResponseEntity<List<UserModel>> findUsersToModifyPermissions() {
        return new ResponseEntity<>(service.getUsersToModifyPermissions(), HttpStatus.OK);
    }


    @GetMapping("/comments/{userId}")
    public ResponseEntity<List<CommentModel>> findUnprocessedComments(
            @PathVariable("userId") Integer userId, HttpServletRequest request) {

        tokenExtractor(request);

        if(!wafService.checkNumberLength(userId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(service.unprocessedComments(userId), HttpStatus.OK);
    }

    @GetMapping("/waiting-requests/{adminId}")
    public ResponseEntity<List<UserModel>> getWaitingRequests(
            @PathVariable("adminId") Integer adminId, HttpServletRequest request) {

        tokenExtractor(request);

        if(!wafService.checkNumberLength(adminId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(service.getWaitingUsers(adminId), HttpStatus.OK);
    }

    @PatchMapping("/update-role")
    public ResponseEntity<UserModel> updateRole(@RequestBody UserModel user, HttpServletRequest request) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException, NotFoundException {

        tokenExtractor(request);

        if(wafService.checkMySQLInjection(user.getUsername()) || wafService.checkMySQLInjection(user.getEmail())
        ||wafService.checkMySQLInjection(user.getPassword()) || wafService.checkMySQLInjection(user.getRole().toString()
        )) {
            return BadEntity.returnForbidden();
        }

        if(wafService.checkXSSInjection(user.getUsername()) || wafService.checkXSSInjection(user.getEmail())
                ||wafService.checkXSSInjection(user.getPassword()) || wafService.checkXSSInjection(user.getRole().toString()
        )) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.checkNumberLength(user.getId(), request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }

        if(wafService.checkIfUserIsAdmin(user.getId())) {
            return BadEntity.returnForbidden();
        }

        byte[] response = wafService.authorizePermissionModification(user.getId(), request.getRequestURI(), MessageHasher.createDigitalSignature(user.getId().toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.update(user), HttpStatus.OK);
    }

    @PatchMapping("/enable-users/{adminId}")
    public ResponseEntity<UserModel> configureUserEnabled(@PathVariable("adminId") Integer adminId,
                                                          @RequestBody UserModel user
            , HttpServletRequest request) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException, NotFoundException {

        tokenExtractor(request);

        if(!wafService.checkNumberLength(adminId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }

        if(wafService.checkMySQLInjection(user.getUsername()) || wafService.checkMySQLInjection(user.getEmail())
                ||wafService.checkMySQLInjection(user.getPassword()) || wafService.checkMySQLInjection(user.getRole().toString()
        )) {
            return BadEntity.returnForbidden();
        }

        if(wafService.checkXSSInjection(user.getUsername()) || wafService.checkXSSInjection(user.getEmail())
                ||wafService.checkXSSInjection(user.getPassword()) || wafService.checkXSSInjection(user.getRole().toString()
        )) {
            return BadEntity.returnForbidden();
        }

        byte[] response = wafService.authorizePermissionModification(user.getId(), request.getRequestURI(), MessageHasher.createDigitalSignature(user.getId().toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.configureUserEnabled(user), HttpStatus.OK);
    }

    @PatchMapping("/disable-users/{adminId}/{userId}")
    public ResponseEntity<UserModel> configureUserDisabled(@PathVariable("adminId") Integer adminId,
                                                           @PathVariable("userId") Integer userId
            , HttpServletRequest request) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NotFoundException, UnrecoverableKeyException, KeyStoreException {

        tokenExtractor(request);

        if(!wafService.checkNumberLength(adminId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }
        if(!wafService.checkNumberLength(userId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }

        byte[] response = wafService.authorizePermissionModification(userId, request.getRequestURI(), MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.configureUserDisabled(userId), HttpStatus.OK);
    }

    @PatchMapping("/enable-comments")
    public ResponseEntity<CommentModel> enableComment(@RequestBody CommentModel comment
            , HttpServletRequest request) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException {

        tokenExtractor(request);

        if(wafService.checkIfCommentIsForbidden(comment)) {
            return BadEntity.returnForbidden();
        }

        if(wafService.checkMySQLInjection(comment.getContent()) || wafService.checkMySQLInjection(comment.getTitle())) {
            return BadEntity.returnForbidden();
        }

        if(wafService.checkXSSInjection(comment.getContent()) || wafService.checkXSSInjection(comment.getTitle())) {
            return BadEntity.returnForbidden();
        }


        byte[] response = wafService.authorizeCommentsEnablingDisabling(comment.getUserId(), request.getRequestURI(),
                MessageHasher.createDigitalSignature(comment.getUserId().toString(),
                        CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }



        return new ResponseEntity<>(service.enableComment(comment), HttpStatus.OK);
    }

    @PatchMapping("/disable-comments")
    public ResponseEntity<CommentModel> disableComment(@RequestBody CommentModel comment
            , HttpServletRequest request) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException {

        tokenExtractor(request);

        /*if(wafService.checkIfCommentIsForbidden(comment)) {
            return BadEntity.returnForbidden();
        }*/

        if(wafService.checkMySQLInjection(comment.getContent()) || wafService.checkMySQLInjection(comment.getTitle())) {
            return BadEntity.returnForbidden();
        }

        if(wafService.checkXSSInjection(comment.getContent()) || wafService.checkXSSInjection(comment.getTitle())) {
            return BadEntity.returnForbidden();
        }

        byte[] response = wafService.authorizeCommentsEnablingDisabling(comment.getUserId(), request.getRequestURI(),
                MessageHasher.createDigitalSignature(comment.getUserId().toString(),
                        CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.disableComment(comment), HttpStatus.OK);
    }
}
