package org.unibl.etf.sni.backend.exception;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.unibl.etf.sni.backend.log.LogService;
import org.unibl.etf.sni.backend.log.Status;

import java.io.IOException;

@Component
public class AccessDeniedExceptionHandler extends OncePerRequestFilter {

    @Value("${token.signing.key}")
    private String jwtSecret;

    @Autowired
    private LogService logService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("In handle!");

        // Extract JWT token from the Authorization header
        String requestedUri = request.getRequestURI();
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
            try {
                // Decode the JWT token and extract user information
                Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
                String username = claims.getSubject();

                logService.insertNewLog("Access Allowed for user: " + username
                        + " to route: " + requestedUri, Status.DANGER);
            } catch (Exception ex) {
                logService.insertNewLog("Invalid token: " + " for route: " + requestedUri, Status.DANGER);
                //response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Invalid Token");
            }
        } else {
            logService.insertNewLog("Access denied - missing token " + " for route: " + requestedUri, Status.DANGER);
            //response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Missing Token");
        }
        filterChain.doFilter(request, response);
    }

}
