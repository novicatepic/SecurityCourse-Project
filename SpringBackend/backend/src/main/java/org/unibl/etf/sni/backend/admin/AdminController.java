package org.unibl.etf.sni.backend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.MessageHasher;
import org.unibl.etf.sni.backend.certificate.SymmetricKeyGenerator;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.log.MessageProcessor;
import org.unibl.etf.sni.backend.user.UserModel;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;
import org.unibl.etf.sni.backend.waf.WAFService;

import javax.crypto.SecretKey;
import java.security.cert.Certificate;
import java.util.List;

import static org.unibl.etf.sni.backend.certificate.MessageHasher.createDigitalSignature;

@CrossOrigin("*")
@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService service;

    @Autowired
    private WAFService wafService;

    Certificate accessControllerCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.acAlias);
    Certificate wafCertificate = CertificateAliasResolver.getCertificateByAlias(CertificateAliasResolver.wafAlias);
    SecretKey key = SymmetricKeyGenerator.readKeyFromFile();

    public AdminController() throws Exception {
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserModel> findUserById(@PathVariable("userId") Integer userId) throws NotFoundException, Exception {

        if(!wafService.checkNumberLength(userId, "/users/{userId}", MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(service.getUserById(userId), HttpStatus.OK);
    }


    @GetMapping("/comments/{userId}")
    public ResponseEntity<List<CommentModel>> findUnprocessedComments(@PathVariable("userId") Integer userId) throws Exception {

        if(!wafService.authorizeCommentModification()) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.checkNumberLength(userId, "/comments/{userId}", MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }



        return new ResponseEntity<>(service.unprocessedComments(userId), HttpStatus.OK);
    }

    @GetMapping("/waiting-requests/{adminId}")
    public ResponseEntity<List<UserModel>> getWaitingRequests(@PathVariable("adminId") Integer adminId) throws Exception {

        if(!wafService.authorizeUserProfileRequests()) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.checkNumberLength(adminId, "/waiting-requests/{adminId}", MessageHasher.createDigitalSignature(adminId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(service.getWaitingUsers(adminId), HttpStatus.OK);
    }

    //potencijalno ce admin id trebati -> todo
    @PatchMapping("/update-role")
    public ResponseEntity<UserModel> updateRole(@RequestBody UserModel user) throws NotFoundException, Exception {

        if(!wafService.authorizeUserProfileRequests()) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.checkNumberLength(user.getId(), "/update-role", MessageHasher.createDigitalSignature(user.getId().toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }

        if(!wafService.authorizePermissionModification(user.getId())) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.update(user), HttpStatus.OK);
    }

    @PatchMapping("/enable-users/{adminId}/{userId}")
    public ResponseEntity<UserModel> configureUserEnabled(@PathVariable("adminId") Integer adminId,
                                                          @PathVariable("userId") Integer userId) throws NotFoundException, Exception {

        if(!wafService.authorizeUserProfileRequests()) {
            return BadEntity.returnForbidden();
        }

        //can't enable himself
        if(!wafService.authorizePermissionModification(adminId)) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(service.configureUserEnabled(userId), HttpStatus.OK);
    }

    @PatchMapping("/disable-users/{adminId}/{userId}")
    public ResponseEntity<UserModel> configureUserDisabled(@PathVariable("adminId") Integer adminId,
                                                           @PathVariable("userId") Integer userId) throws NotFoundException, Exception {

        if(!wafService.authorizeUserProfileRequests()) {
            return BadEntity.returnForbidden();
        }
        if(!wafService.checkNumberLength(adminId, "/waiting-requests/{adminId}", MessageHasher.createDigitalSignature(adminId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }
        if(!wafService.checkNumberLength(userId, "/waiting-requests/{adminId}", MessageHasher.createDigitalSignature(adminId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }

        if(!wafService.authorizePermissionModification(adminId)) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(service.configureUserDisabled(userId), HttpStatus.OK);
    }

    @PatchMapping("/enable-comments")
    public ResponseEntity<CommentModel> enableComment(@RequestBody CommentModel comment) {
        if(!wafService.authorizeCommentModification()) {
            return BadEntity.returnForbidden();
        }
        //System.out.println("Can");
        if(!wafService.authorizeCommentsEnablingDisabling(comment.getUserId())) {
            return BadEntity.returnForbidden();
        }
        //System.out.println("Or maybe not!");
        return new ResponseEntity<>(service.enableComment(comment), HttpStatus.OK);
    }

    @PatchMapping("/disable-comments")
    public ResponseEntity<CommentModel> disableComment(@RequestBody CommentModel comment) {
        if(!wafService.authorizeCommentModification()) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.authorizeCommentsEnablingDisabling(comment.getUserId())) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.disableComment(comment), HttpStatus.OK);
    }
}
