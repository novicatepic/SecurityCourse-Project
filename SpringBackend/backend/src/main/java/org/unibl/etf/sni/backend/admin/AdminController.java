package org.unibl.etf.sni.backend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.user.UserModel;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService service;

    @PostMapping("/enable-users/{adminId}/{userId}")
    public ResponseEntity<UserModel> configureUserEnabled(@PathVariable("adminId") Integer adminId,
                                                          @PathVariable("userId") Integer userId) throws NotFoundException {
        return new ResponseEntity<>(service.configureUserEnabled(userId), HttpStatus.OK);
    }

    @PostMapping("/disable-users/{adminId}/{userId}")
    public ResponseEntity<UserModel> configureUserDisabled(@PathVariable("adminId") Integer adminId,
                                                           @PathVariable("userId") Integer userId) throws NotFoundException {
        return new ResponseEntity<>(service.configureUserDisabled(userId), HttpStatus.OK);
    }

    @PostMapping("/configure-comments/{adminId}")
    public ResponseEntity<CommentModel> configureComment(@RequestBody CommentModel comment, @PathVariable("adminId") Integer adminId) {
        return new ResponseEntity<>(service.configureComment(comment), HttpStatus.OK);
    }

    @PostMapping("/configure-registrations/{adminId}/{userId}")
    public ResponseEntity<UserModel> configurePermissions(@PathVariable("adminId") Integer adminId,
                                                          @RequestBody UserModel user) throws NotFoundException {
        return new ResponseEntity<>(service.configureUser(user), HttpStatus.OK);
    }

}
