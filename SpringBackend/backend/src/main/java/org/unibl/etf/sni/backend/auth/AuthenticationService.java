package org.unibl.etf.sni.backend.auth;


import org.apache.catalina.User;
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
import org.unibl.etf.sni.backend.role.Role;
import org.unibl.etf.sni.backend.user.*;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Autowired
    private CodeService codeService;
    @Autowired
    private MailService mailService;


    public AuthenticationService(UserRepository userRepository,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public BoolAuthResponse loginUNPW(AuthRequest request) throws InvalidUsernameException, NotFoundException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserModel user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidUsernameException::new);

        if(!user.getActive()) {
            return new BoolAuthResponse(false, user.getId());
        }

        return loginCredentials(request);
    }

    private BoolAuthResponse loginCredentials(AuthRequest request) throws InvalidUsernameException, NotFoundException {
        UserModel k = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidUsernameException::new);

        return getBoolAuthResponse(k);

    }

    public JwtAuthResponse codeEntrance(Code code) throws NotFoundException {

        Code c = codeService.getById(code.getUserId());

        if(c == null) {
            return null;
        }

        if(!c.getCode().equals(code.getCode())) {
            return null;
        }

        codeService.deleteCode(c.getUserId());

        var user = userRepository.findById(code.getUserId());
        UserModel extractedUser;
        if(!user.isPresent()) {
            throw new NotFoundException();
        }
        extractedUser = user.get();
        var jwt = jwtService.generateToken(extractedUser);
        JwtAuthResponse response = new JwtAuthResponse(jwt);
        return response;


    }


    public JwtAuthResponse githubMailLogin(String email, String username)  {
        /*Optional<GithubUserModel> k = githubUserRepository.findByEmail(email);

        if(k.isPresent()) {
            GithubUserModel user = k.get();
            if(user.getActive()) {
                String jwt = jwtService.generateTokenGithub(user);
                JwtAuthResponse response = new JwtAuthResponse(jwt);
                return response;
            }
            return null;
        }*/

        Optional<UserModel> k = userRepository.findByEmail(email);

        if(k.isPresent()) {
            UserModel user = k.get();
            if(user.getActive()) {
                String jwt = jwtService.generateToken(user);
                JwtAuthResponse response = new JwtAuthResponse(jwt);
                return response;
            }
            return null;
        }

        UserModel userModel = new UserModel();
        userModel.setActive(false);
        userModel.setRole(Role.ROLE_UNDEFINED);
        userModel.setUsername(username);
        userModel.setEmail(email);
        userModel.setPassword("github_user");
        userRepository.save(userModel);

        /*GithubUserModel userModel = new GithubUserModel();
        userModel.setActive(false);
        userModel.setRole(Role.ROLE_UNDEFINED);
        userModel.setEmail(email);
        githubUserRepository.save(userModel);*/
        //System.out.println("Returned null");

        return null;
    }

    public JwtAuthResponse githubLogin(UserModel user) {
        String jwt = jwtService.generateToken(user);
        JwtAuthResponse response = new JwtAuthResponse(jwt);
        return response;
    }

    private BoolAuthResponse getBoolAuthResponse(UserModel k) throws NotFoundException {
        if(k != null && k.getActive()) {

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



}
