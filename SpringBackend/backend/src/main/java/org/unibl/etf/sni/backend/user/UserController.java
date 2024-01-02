package org.unibl.etf.sni.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.exception.NotFoundException;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserModel> registerUser(@RequestBody UserModel userModel) {
        return new ResponseEntity<>(userService.registerUser(userModel), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> findUserById(@PathVariable("userId") Integer userId) throws NotFoundException {
        return new ResponseEntity<>(userService.findByUserId(userId), HttpStatus.OK);
    }
}
