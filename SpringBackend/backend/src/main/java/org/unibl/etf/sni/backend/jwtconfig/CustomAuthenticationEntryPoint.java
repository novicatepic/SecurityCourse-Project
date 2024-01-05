package org.unibl.etf.sni.backend.jwtconfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.unibl.etf.sni.backend.log.LogService;
import org.unibl.etf.sni.backend.log.Status;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @Autowired
    private LogService logService;

    @Autowired
    private TokenBlackListService tokenBlackListService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String token = request.getHeader("Authorization");
        String username = "Anonymous"; // Default to Anonymous if not present in token

        if (token != null && token.startsWith("Bearer ")) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSigningKey) // Replace with your actual secret key
                        .parseClaimsJws(token.replace("Bearer ", ""))
                        .getBody();

                username = claims.getSubject();
            } catch (SignatureException e) {
                // Handle invalid or expired token
            }
        }

        if(token != null) {
            System.out.println("ERROR NOTICED");
            String errorMessage = "Authorization failed for user '" + username;
            String tokenToAdd = token.substring(7);
            tokenBlackListService.addToBlacklist(tokenToAdd);
            System.out.println("Token added " + tokenToAdd);
            logService.insertNewLog(errorMessage, Status.DANGER);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }


        //response.getWriter().write(errorMessage);
    }
}
