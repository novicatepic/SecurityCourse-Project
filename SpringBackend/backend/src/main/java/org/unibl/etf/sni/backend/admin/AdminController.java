package org.unibl.etf.sni.backend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.user.UserModel;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService service;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserModel> findUserById(@PathVariable("userId") Integer userId) throws NotFoundException {
        return new ResponseEntity<>(service.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentModel>> findUnprocessedComments() throws NotFoundException {
        return new ResponseEntity<>(service.unprocessedComments(), HttpStatus.OK);
    }

    @GetMapping("/waiting-requests/{adminId}")
    public ResponseEntity<List<UserModel>> getWaitingRequests(@PathVariable("adminId") Integer adminId) {
        return new ResponseEntity<>(service.getWaitingUsers(), HttpStatus.OK);
    }

    @PatchMapping("/update-role")
    public ResponseEntity<UserModel> updateRole(@RequestBody UserModel user) throws NotFoundException {
        System.out.println("In");
        return new ResponseEntity<>(service.update(user), HttpStatus.OK);
    }

    @PatchMapping("/enable-users/{adminId}/{userId}")
    public ResponseEntity<UserModel> configureUserEnabled(@PathVariable("adminId") Integer adminId,
                                                          @PathVariable("userId") Integer userId) throws NotFoundException {
        return new ResponseEntity<>(service.configureUserEnabled(userId), HttpStatus.OK);
    }

    @PatchMapping("/disable-users/{adminId}/{userId}")
    public ResponseEntity<UserModel> configureUserDisabled(@PathVariable("adminId") Integer adminId,
                                                           @PathVariable("userId") Integer userId) throws NotFoundException {
        return new ResponseEntity<>(service.configureUserDisabled(userId), HttpStatus.OK);
    }

    @PatchMapping("/enable-comments")
    public ResponseEntity<CommentModel> enableComment(@RequestBody CommentModel comment) {
        return new ResponseEntity<>(service.enableComment(comment), HttpStatus.OK);
    }

    @PatchMapping("/disable-comments")
    public ResponseEntity<CommentModel> disableComment(@RequestBody CommentModel comment) {
        return new ResponseEntity<>(service.disableComment(comment), HttpStatus.OK);
    }

    /*@PatchMapping("/configure-registrations/{adminId}/{userId}")
    public ResponseEntity<UserModel> configurePermissions(@PathVariable("adminId") Integer adminId,
                                                          @RequestBody UserModel user) throws NotFoundException {
        return new ResponseEntity<>(service.configureUser(user), HttpStatus.OK);
    }*/

}
