package org.unibl.etf.sni.backend.jwtconfig;

import jakarta.servlet.http.HttpServletRequest;

public class TokenExtractor {

    public static String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
