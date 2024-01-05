package org.unibl.etf.sni.backend.jwtconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.unibl.etf.sni.backend.exception.AccessDeniedExceptionHandler;
import org.unibl.etf.sni.backend.user.UserService;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter filter) {
        this.jwtAuthenticationFilter = filter;
    }


    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private UserService userService;

    @Autowired
    private AccessDeniedExceptionHandler accessDeniedHandler;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {;
        http.exceptionHandling((exception)-> exception.authenticationEntryPoint(customAuthenticationEntryPoint));
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> {
                            try {
                                authorize
                                        .requestMatchers("/csrf").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll() //Authentication controller
                                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll() //UserController

                                        //.requestMatchers("/comments/test").permitAll()

                                        .requestMatchers(HttpMethod.GET, "/comments/{commentId}/{userId}").hasAnyRole("ADMIN", "MODERATOR")
                                        .requestMatchers("/comments/**").hasAnyRole("ADMIN", "MODERATOR", "FORUM") //CommentController
                                        .requestMatchers("/rooms/**").hasAnyRole("ADMIN", "MODERATOR", "FORUM") //RoomController
                                        .requestMatchers("/permissions/{roomId}/{userId}").hasAnyRole("ADMIN", "MODERATOR", "FORUM") //RoomController
                                        .requestMatchers("/users/{userId}").hasAnyRole("ADMIN", "MODERATOR", "FORUM") //UserController

                                        .requestMatchers("/admins/disable-comments").hasAnyRole("ADMIN", "MODERATOR")
                                        .requestMatchers("/admins/enable-comments").hasAnyRole("ADMIN", "MODERATOR") //UserRoomPermissionController


                                        .requestMatchers("/admins/users/**").permitAll()//.hasRole("ADMIN") //AdminController all until UserRoomPermissionController
                                        .requestMatchers("/admins/waiting-requests/**").hasRole("ADMIN")
                                        .requestMatchers("/admins/update-role").hasRole("ADMIN")
                                        .requestMatchers("/admins/enable-users/**").hasRole("ADMIN")
                                        .requestMatchers("/admins/disable-users/**").hasRole("ADMIN")
                                        .requestMatchers("/admins/users/**").hasRole("ADMIN")
                                        .requestMatchers("/permissions/**").hasRole("ADMIN") //UserRoomPermissionController
                                        .requestMatchers(HttpMethod.GET, "/users/{userId}").hasRole("ADMIN") //UserController
                                        .anyRequest().authenticated();




                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

}
