package org.unibl.etf.sni.backend.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.unibl.etf.sni.backend.comment.CommentModel;
import org.unibl.etf.sni.backend.role.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", schema = "sni_project", catalog = "")
public class UserModel implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank(message = "username for user is mandatory!")
    @Size(max = 45, message = "Maximum character size for username is 45!")
    @Basic
    @Column(name = "username", nullable = false, length = 45, unique = true)
    private String username;

    @NotBlank(message = "password for user is mandatory!")
    @Size(max = 500, message = "Maximum character size for password is 500!")
    @Basic
    @Column(name = "password", nullable = false, length = 500)
    private String password;

    @NotNull(message = "role for user is mandatory!")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role;

    @Email
    @NotBlank(message = "email for user is mandatory!")
    @Size(max = 200, message = "Maximum character size for email is 200!")
    @Basic
    @Column(name = "email", nullable = false, length = 200, unique = true)
    private String email;

    @NotNull
    @Basic
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
