package org.unibl.etf.sni.backend.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.exception.PasswordTooShortException;
import org.unibl.etf.sni.backend.exception.RegistrationNotAllowed;
import org.unibl.etf.sni.backend.jwtconfig.TokenExtractor;
import org.unibl.etf.sni.backend.waf.WAFService;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WAFService wafService;

    @PostMapping("/register")
    public ResponseEntity<UserModel> registerUser(@RequestBody UserModel userModel,
                                                  HttpServletRequest request)
                                                    throws RegistrationNotAllowed, PasswordTooShortException {

        tokenExtractor(request);

        if(wafService.checkMySQLInjection(userModel.getPassword()) || wafService.checkMySQLInjection(userModel.getUsername())
                || wafService.checkMySQLInjection(userModel.getEmail()) || wafService.checkMySQLInjection(userModel.getRole().toString())
        ) {
            return BadEntity.returnForbidden();
        }

        if(wafService.checkXSSInjection(userModel.getPassword()) || wafService.checkXSSInjection(userModel.getUsername())
                || wafService.checkXSSInjection(userModel.getEmail()) || wafService.checkXSSInjection(userModel.getRole().toString())
        ) {
            return BadEntity.returnForbidden();
        }
        return new ResponseEntity<>(userService.registerUser(userModel), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> findUserById(@PathVariable("userId") Integer userId,
                                                  HttpServletRequest request) throws NotFoundException {

        tokenExtractor(request);

        if(!wafService.checkNumberLength(userId, request.getRequestURI())) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(userService.findByUserId(userId), HttpStatus.OK);
    }

    private void tokenExtractor(HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        wafService.setToken(token);
    }
}
