package org.unibl.etf.sni.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.exception.NotFoundException;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserModel registerUser(UserModel user) {
        user.setPassword(passwordEncoder().encode(user.getPassword()) );
        user.setActive(false);
        user.setIsTerminated(false);
        return userRepository.save(user);
    }

    public UserModel findByUserId(Integer id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

}
