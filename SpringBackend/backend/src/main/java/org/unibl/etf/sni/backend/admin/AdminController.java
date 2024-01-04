package org.unibl.etf.sni.backend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.MessageHasher;
import org.unibl.etf.sni.backend.certificate.SymmetricKeyGenerator;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.jsonconverter.JSONConverter;
import org.unibl.etf.sni.backend.log.MessageProcessor;
import org.unibl.etf.sni.backend.user.UserModel;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;
import org.unibl.etf.sni.backend.waf.WAFService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.util.List;

import static org.unibl.etf.sni.backend.certificate.MessageHasher.createDigitalSignature;

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
    public ResponseEntity<UserModel> findUserById(@PathVariable("userId") Integer userId) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException, NotFoundException {

        if(!wafService.checkNumberLength(userId, "/users/{userId}", MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(service.getUserById(userId), HttpStatus.OK);
    }


    @GetMapping("/comments/{userId}")
    public ResponseEntity<List<CommentModel>> findUnprocessedComments(@PathVariable("userId") Integer userId) throws Exception {

        /*if(!wafService.authorizeCommentModification()) {
            return BadEntity.returnForbidden();
        }*/

        if(!wafService.checkNumberLength(userId, "/comments/{userId}", MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }



        return new ResponseEntity<>(service.unprocessedComments(userId), HttpStatus.OK);
    }

    @GetMapping("/waiting-requests/{adminId}")
    public ResponseEntity<List<UserModel>> getWaitingRequests(@PathVariable("adminId") Integer adminId) throws Exception {

        if(!wafService.checkNumberLength(adminId, "/waiting-requests/{adminId}", MessageHasher.createDigitalSignature(adminId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(service.getWaitingUsers(adminId), HttpStatus.OK);
    }

    @PatchMapping("/update-role")
    public ResponseEntity<UserModel> updateRole(@RequestBody UserModel user) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException, NotFoundException {

        String jsonObject = JSONConverter.convertObjectToString(user);
        byte[] responseXSSSQL = wafService.checkObjectValidity(jsonObject, "/enable-comments", MessageHasher.createDigitalSignature(jsonObject,
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), responseXSSSQL, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.checkNumberLength(user.getId(), "/update-role", MessageHasher.createDigitalSignature(user.getId().toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }


        byte[] response = wafService.authorizePermissionModification(user.getId(), "/update-role", MessageHasher.createDigitalSignature(user.getId().toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.update(user), HttpStatus.OK);
    }

    @PatchMapping("/enable-users/{adminId}")
    public ResponseEntity<UserModel> configureUserEnabled(@PathVariable("adminId") Integer adminId,
                                                          @RequestBody UserModel user) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException, NotFoundException {


        byte[] response = wafService.authorizePermissionModification(user.getId(), "/update-role", MessageHasher.createDigitalSignature(user.getId().toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.configureUserEnabled(user), HttpStatus.OK);
    }

    @PatchMapping("/disable-users/{adminId}/{userId}")
    public ResponseEntity<UserModel> configureUserDisabled(@PathVariable("adminId") Integer adminId,
                                                           @PathVariable("userId") Integer userId) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NotFoundException, UnrecoverableKeyException, KeyStoreException {

        if(!wafService.checkNumberLength(adminId, "/waiting-requests/{adminId}", MessageHasher.createDigitalSignature(adminId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }
        if(!wafService.checkNumberLength(userId, "/waiting-requests/{adminId}", MessageHasher.createDigitalSignature(adminId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }

        byte[] response = wafService.authorizePermissionModification(userId, "/update-role", MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.configureUserDisabled(userId), HttpStatus.OK);
    }

    @PatchMapping("/enable-comments")
    public ResponseEntity<CommentModel> enableComment(@RequestBody CommentModel comment) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException {

        String jsonObject = JSONConverter.convertObjectToString(comment);
        byte[] responseXSSSQL = wafService.checkObjectValidity(jsonObject, "/enable-comments", MessageHasher.createDigitalSignature(jsonObject,
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), responseXSSSQL, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }


        byte[] response = wafService.authorizeCommentsEnablingDisabling(comment.getUserId(), "/enable-comments",
                MessageHasher.createDigitalSignature(comment.getUserId().toString(),
                        CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }



        return new ResponseEntity<>(service.enableComment(comment), HttpStatus.OK);
    }

    @PatchMapping("/disable-comments")
    public ResponseEntity<CommentModel> disableComment(@RequestBody CommentModel comment) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException {

        String jsonObject = JSONConverter.convertObjectToString(comment);
        byte[] responseXSSSQL = wafService.checkObjectValidity(jsonObject, "/enable-comments", MessageHasher.createDigitalSignature(jsonObject,
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), responseXSSSQL, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        /*if(!wafService.authorizeCommentModification()) {
            return BadEntity.returnForbidden();
        }*/

        byte[] response = wafService.authorizeCommentsEnablingDisabling(comment.getUserId(), "/disable-comments",
                MessageHasher.createDigitalSignature(comment.getUserId().toString(),
                        CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.disableComment(comment), HttpStatus.OK);
    }
}
