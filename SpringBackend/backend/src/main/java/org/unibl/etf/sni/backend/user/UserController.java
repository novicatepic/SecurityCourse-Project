package org.unibl.etf.sni.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.certificate.CertificateAliasResolver;
import org.unibl.etf.sni.backend.certificate.MessageHasher;
import org.unibl.etf.sni.backend.certificate.Validator;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.jsonconverter.JSONConverter;
import org.unibl.etf.sni.backend.protocol.ProtocolMessages;
import org.unibl.etf.sni.backend.waf.WAFService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WAFService wafService;

    @PostMapping("/register")
    public ResponseEntity<UserModel> registerUser(@RequestBody UserModel userModel) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException {

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
    public ResponseEntity<UserModel> findUserById(@PathVariable("userId") Integer userId) throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, KeyStoreException, BadPaddingException, InvalidKeyException, NotFoundException {
        if(!wafService.checkNumberLength(userId, "/users/"+userId)) {
            return BadEntity.returnBadRequst();
        }

        return new ResponseEntity<>(userService.findByUserId(userId), HttpStatus.OK);
    }
}
