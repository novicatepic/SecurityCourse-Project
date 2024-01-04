package org.unibl.etf.sni.backend.comment;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.MessageHasher;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;
import org.unibl.etf.sni.backend.waf.WAFService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

//@CrossOrigin("*")
@CrossOrigin(origins = "https://localhost:4200")
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService service;

    @Autowired
    private WAFService wafService;

    @GetMapping("/test")
    public String getText() {
        return "abc";
    }

    @GetMapping("/{commentId}/{userId}")
    public ResponseEntity<CommentModel> getCommentById(@PathVariable("commentId") Integer commentId,
                                                       @PathVariable("userId") Integer userId)
            throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException, NotFoundException {


        byte[] response = wafService.authorizeUserId(userId, "/{commentId}/{userId}", MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        /*if(!wafService.authorizeCommentModification()) {
            return BadEntity.returnForbidden();
        }*/

        /*if(!wafService.checkNumberLength(commentId, "/{commentId}/{userId}/{roomId}", MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }*/

        return new ResponseEntity<>(service.findCommentById(commentId), HttpStatus.OK);
    }

    /*@PostMapping("/mock")
    public void createComments()   {
        for(int i=1; i<=20; i++) {
            CommentModel comment = new CommentModel();
            comment.setRoomId(1);
            comment.setEnabled(true);
            comment.setForbidden(false);
            comment.setContent("Contentt"+i);
            comment.setTitle("Titlee"+i);
            comment.setDateCreated(new Date(2024, 1, 2));
            comment.setUserId(3);
            service.createComment(comment);
        }
    }*/

    @PostMapping
    public ResponseEntity<CommentModel> createComment(@Valid @RequestBody CommentModel commentModel)
            throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException, NotFoundException {

        //can't create comment for someone else
        /*if(!wafService.authorizeUserId(commentModel.getUserId())) {
            return BadEntity.returnForbidden();
        }*/

        if(wafService.checkMySQLInjection(commentModel.getContent()) || wafService.checkMySQLInjection(commentModel.getTitle())) {
            return BadEntity.returnForbidden();
        }

        if(wafService.checkXSSInjection(commentModel.getContent()) || wafService.checkXSSInjection(commentModel.getTitle())) {
            return BadEntity.returnForbidden();
        }

        byte[] response = wafService.authorizeUserId(commentModel.getUserId(), "/comments", MessageHasher.createDigitalSignature(commentModel.getUserId().toString(),
                CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), response, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }


        /*if(!wafService.authorizeCreationUserPermissionsForRoomAndComment(commentModel.getRoomId(), commentModel.getUserId())) {
            return BadEntity.returnForbidden();
        }*/
        byte[] commentResponse = wafService.authorizeCreationUserPermissionsForRoomAndComment(commentModel.getRoomId(),
                commentModel.getUserId(), "/comments", MessageHasher.createDigitalSignature(commentModel.getRoomId().toString(),
                CertificateAliasResolver.acAlias),
                MessageHasher.createDigitalSignature(commentModel.getUserId().toString(),
                        CertificateAliasResolver.acAlias));
        if(commentResponse == null) {
            System.out.println("Null");
        } else {
            System.out.println("Not null");
        }
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), commentResponse, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.createComment(commentModel), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/{userId}/{roomId}")
    public ResponseEntity<SuccessOperation> deleteComment(@PathVariable("commentId") Integer commentId,
                                                          @PathVariable("userId")Integer userId,
                                                          @PathVariable("roomId")Integer roomId) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException, NotFoundException {


        byte[] commentResponse = wafService.authorizeDeleteUserPermissionsForRoomAndComment(roomId,
                userId, commentId,"/{commentId}/{userId}/{roomId}", MessageHasher.createDigitalSignature(roomId.toString(),
                        CertificateAliasResolver.acAlias),
                MessageHasher.createDigitalSignature(userId.toString(),
                        CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), commentResponse, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.deleteComment(commentId), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<CommentModel> updateComment(@Valid @RequestBody CommentModel commentModel)
            throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException, NotFoundException {

        if(wafService.checkMySQLInjection(commentModel.getContent()) || wafService.checkMySQLInjection(commentModel.getTitle())) {
            return BadEntity.returnForbidden();
        }

        if(wafService.checkXSSInjection(commentModel.getContent()) || wafService.checkXSSInjection(commentModel.getTitle())) {
            return BadEntity.returnForbidden();
        }

        byte[] commentResponse = wafService.authorizeUpdateUserPermissionsForRoomAndComment(commentModel.getRoomId(),
                commentModel.getUserId(), commentModel.getId(),
                "/{commentId}/{userId}/{roomId}",
                MessageHasher.createDigitalSignature(commentModel.getRoomId().toString(),
                        CertificateAliasResolver.acAlias),
                MessageHasher.createDigitalSignature(commentModel.getUserId().toString(),
                        CertificateAliasResolver.acAlias));
        if(!Validator.checkMessageValidity(ProtocolMessages.OK.toString(), commentResponse, WAFService.wafCertificate)) {
            return BadEntity.returnForbidden();
        }


        return new ResponseEntity<>(service.createComment(commentModel), HttpStatus.OK);
    }

}
