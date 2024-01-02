package org.unibl.etf.sni.backend.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.code.Code;
import org.unibl.etf.sni.backend.code.CodeService;
import org.unibl.etf.sni.backend.exception.InvalidUsernameException;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.jwtconfig.JwtService;
import org.unibl.etf.sni.backend.mail.MailService;
import org.unibl.etf.sni.backend.user.UserModel;
import org.unibl.etf.sni.backend.user.UserRepository;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Autowired
    private CodeService codeService;
    @Autowired
    private MailService mailService;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public BoolAuthResponse loginUNPW(AuthRequest request) throws InvalidUsernameException, NotFoundException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserModel user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidUsernameException::new);

        if(!user.getActive() || user.getIsTerminated()) {
            return new BoolAuthResponse(false, user.getId());
        }
        System.out.println("HERE 1");
        return loginCredentials(request);
    }

    private BoolAuthResponse loginCredentials(AuthRequest request) throws InvalidUsernameException, NotFoundException {
        UserModel k = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidUsernameException::new);
        if(k != null && k.getActive() && !k.getIsTerminated()) {
            Code codeFromDatabase = codeService.getById(k.getId());
            if(codeFromDatabase != null) {
                codeService.deleteCode(k.getId());
            }
            String code = codeService.saveCodeToDB(k);
            mailService.sendEmail(k.getEmail(), "Code for logging in", code);
            return new BoolAuthResponse(true, k.getId());
        }
        return new BoolAuthResponse(false, 0);

    }

    public JwtAuthResponse codeEntrance(Code code) throws NotFoundException {

        Code c = codeService.getById(code.getUserId());
        //System.out.println("Code " + c.getCode());
        if(c == null) {
            return null;
        }

        codeService.deleteCode(c.getUserId());

        if(!c.getCode().equals(code.getCode())) {
            return null;
        }
        System.out.println("Equals" );
        var user = userRepository.findById(code.getUserId());
        UserModel extractedUser;
        if(!user.isPresent()) {
            throw new NotFoundException();
        }
        System.out.println("Found user" );
        extractedUser = user.get();
        var jwt = jwtService.generateToken(extractedUser);
        JwtAuthResponse response = new JwtAuthResponse(jwt);
        System.out.println("Token " + response );
        return response;


    }
}
