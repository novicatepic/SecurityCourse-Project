package org.unibl.etf.sni.backend.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.exception.NotFoundException;

@CrossOrigin("*")
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService service;

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentModel> getCommentById(@PathVariable("commentId") Integer commentId) throws NotFoundException  {
        return new ResponseEntity<>(service.findCommentById(commentId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentModel> createComment(@RequestBody CommentModel commentModel)   {
        return new ResponseEntity<>(service.createComment(commentModel), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessOperation> deleteComment(@PathVariable("commentId") Integer commentId) {
        return new ResponseEntity<>(service.deleteComment(commentId), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<CommentModel> deleteComment(@RequestBody CommentModel commentModel) throws NotFoundException  {
        return new ResponseEntity<>(service.createComment(commentModel), HttpStatus.OK);
    }

}
