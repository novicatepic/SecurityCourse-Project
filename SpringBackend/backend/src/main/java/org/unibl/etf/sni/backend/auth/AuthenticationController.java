package org.unibl.etf.sni.backend.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.authorization.BadEntity;
import org.unibl.etf.sni.backend.code.Code;
import org.unibl.etf.sni.backend.exception.InvalidUsernameException;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.jwtconfig.TokenExtractor;
import org.unibl.etf.sni.backend.waf.WAFService;

//@CrossOrigin("*")
@CrossOrigin(origins = "https://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Autowired
    private WAFService wafService;

    @PostMapping("/login")
    public ResponseEntity<BoolAuthResponse> upLogin(@Valid @RequestBody AuthRequest request) throws InvalidUsernameException, NotFoundException {

        if(wafService.checkMySQLInjection(request.getUsername()) || wafService.checkMySQLInjection(request.getPassword())
        ) {
            return BadEntity.returnForbidden();
        }

        if(wafService.checkXSSInjection(request.getUsername()) || wafService.checkXSSInjection(request.getPassword())
        ) {
            return BadEntity.returnForbidden();
        }

        BoolAuthResponse response = authenticationService.loginUNPW(request);

        if(!response.isSuccess()) {
            wafService.handleBadLogin(request.getUsername());
        } else {
            wafService.handleGoodLogin(request.getUsername());
        }

        return ResponseEntity.ok(response);

    }

    @PostMapping("/code")
    public ResponseEntity<JwtAuthResponse> codeEntrance(@Valid @RequestBody Code code) throws  NotFoundException {

        if(wafService.checkMySQLInjection(code.getCode())
        ) {
            return BadEntity.returnForbidden();
        }

        if(wafService.checkXSSInjection(code.getCode())
        ) {
            return BadEntity.returnForbidden();
        }

        JwtAuthResponse response = authenticationService.codeEntrance(code);

        if(response == null) {
            wafService.handleBadCode(code);
        } else {
            wafService.handleGoodCode(code);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        tokenExtractor(request);

        wafService.logoutUser();

    }

    private void tokenExtractor(HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        wafService.setToken(token);
    }

}
