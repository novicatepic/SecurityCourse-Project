package org.unibl.etf.sni.backend.comment;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.MessageHasher;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.waf.WAFService;

@CrossOrigin("*")
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService service;

    @Autowired
    private WAFService wafService;

    @GetMapping("/{commentId}/{userId}")
    public ResponseEntity<CommentModel> getCommentById(@PathVariable("commentId") Integer commentId,
                                                       @PathVariable("userId") Integer userId)
            throws NotFoundException, Exception  {

        if(!wafService.authorizeUserId(userId)) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.authorizeCommentModification()) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.checkNumberLength(commentId, "/{commentId}/{userId}/{roomId}", MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }

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
    public ResponseEntity<CommentModel> createComment(@Valid @RequestBody CommentModel commentModel) throws NotFoundException  {

        //can't create comment for someone else
        if(!wafService.authorizeUserId(commentModel.getUserId())) {
            return BadEntity.returnForbidden();
        }



        //need to have any of the roles
        if(!wafService.authorizeCommentCUD()) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.authorizeCreationUserPermissionsForRoomAndComment(commentModel.getRoomId(), commentModel.getUserId())) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.createComment(commentModel), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/{userId}/{roomId}")
    public ResponseEntity<SuccessOperation> deleteComment(@PathVariable("commentId") Integer commentId,
                                                          @PathVariable("userId")Integer userId,
                                                          @PathVariable("roomId")Integer roomId) throws NotFoundException, Exception {

        if(!wafService.checkNumberLength(userId, "/{commentId}/{userId}/{roomId}", MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }
        if(!wafService.checkNumberLength(commentId, "/{commentId}/{userId}/{roomId}", MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }
        if(!wafService.checkNumberLength(roomId, "/{commentId}/{userId}/{roomId}", MessageHasher.createDigitalSignature(userId.toString(),
                CertificateAliasResolver.acAlias))) {
            return BadEntity.returnBadRequst();
        }

        //can't create comment for someone else
        if(!wafService.authorizeUserId(userId)) {
            return BadEntity.returnForbidden();
        }

        //need to have any of the roles
        if(!wafService.authorizeCommentCUD()) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.authorizeDeleteUserPermissionsForRoomAndComment(roomId, userId)) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.deleteComment(commentId), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<CommentModel> updateComment(@Valid @RequestBody CommentModel commentModel) throws NotFoundException {
        //can't create comment for someone else
        if(!wafService.authorizeUserId(commentModel.getUserId())) {
            return BadEntity.returnForbidden();
        }

        //need to have any of the roles
        if(!wafService.authorizeCommentCUD()) {
            return BadEntity.returnForbidden();
        }

        if(!wafService.authorizeUpdateUserPermissionsForRoomAndComment(commentModel.getRoomId(),
                commentModel.getUserId())) {
            return BadEntity.returnForbidden();
        }

        return new ResponseEntity<>(service.createComment(commentModel), HttpStatus.OK);
    }

}
