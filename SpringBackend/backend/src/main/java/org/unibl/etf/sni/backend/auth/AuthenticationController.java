package org.unibl.etf.sni.backend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.sni.backend.code.Code;
import org.unibl.etf.sni.backend.exception.InvalidUsernameException;
import org.unibl.etf.sni.backend.exception.NotFoundException;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<BoolAuthResponse> upLogin(@RequestBody AuthRequest request) throws InvalidUsernameException, NotFoundException {

        BoolAuthResponse response = authenticationService.loginUNPW(request);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/code")
    public ResponseEntity<JwtAuthResponse> codeEntrance(@RequestBody Code code) throws InvalidUsernameException, NotFoundException {

        JwtAuthResponse response = authenticationService.codeEntrance(code);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
