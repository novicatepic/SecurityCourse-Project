package org.unibl.etf.sni.backend.jwtconfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.unibl.etf.sni.backend.user.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtService jwtService, UserService service) {
        this.jwtService = jwtService;
        this.userService = service;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        username = jwtService.extractUserName(jwt);
        if (!StringUtils.isEmpty(username)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);

                String requestedUri = request.getRequestURI();
                boolean hasPermission = checkUserPermission(userDetails, requestedUri, request);

                System.out.println("Has permissions " + hasPermission + " for " + requestedUri);

            }
        } else {
            handleUnauthorizedAccess(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkUserPermission(UserDetails userDetails, String requestedUri, HttpServletRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        FilterInvocation filterInvocation = new FilterInvocation(requestedUri, request.getMethod());

        System.out.println("Name = " + authentication.getName());

        try {
            AccessDecisionManager accessDecisionManager = accessDecisionManager(); // Inject or create your AccessDecisionManager
            accessDecisionManager.decide(authentication, filterInvocation, Collections.emptyList());
            return true; // If no exceptions are thrown, the user has permission
        } catch (AccessDeniedException e) {
            return false; // Access is denied
        }
    }

    private AccessDecisionManager accessDecisionManager() {
        // Implement or inject your AccessDecisionManager here
        // You might need to customize this based on your security configuration

        List<AccessDecisionVoter<?>> decisionVoters = Arrays.asList(new WebExpressionVoter(), new RoleVoter(), new AuthenticatedVoter());
        return new AffirmativeBased(decisionVoters);
    }

    private void handleUnauthorizedAccess(HttpServletRequest request, HttpServletResponse response) {
        // Log or handle unauthorized access here
        // For example, you can log the request details or send a custom response to the client
        //log.warn("Unauthorized access attempt to URI: {}", request.getRequestURI());

        System.out.println("Bad request!");

        // You can send a custom response to the client, e.g., response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Access");
    }

}
