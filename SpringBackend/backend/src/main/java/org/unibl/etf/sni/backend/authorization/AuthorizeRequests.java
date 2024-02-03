package org.unibl.etf.sni.backend.authorization;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.unibl.etf.sni.backend.user.UserModel;

public class AuthorizeRequests {

    private static UserModel extractCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel userDetails = (UserModel) authentication.getPrincipal();
        return userDetails;
    }

    public static boolean checkIdValidity(Integer id) {
        UserModel k = extractCredentials();

        if(k.getId() != id) {
            return false;
        }
        return true;
    }
}
