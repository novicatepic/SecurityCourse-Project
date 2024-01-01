package org.unibl.etf.sni.backend.user;

import jakarta.persistence.*;
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

    @Basic
    @Column(name = "username", nullable = false, length = 45, unique = true)
    private String username;

    @Basic
    @Column(name = "password", nullable = false, length = 500)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Basic
    @Column(name = "email", nullable = false, length = 200, unique = true)
    private String email;

    @Basic
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Basic
    @Column(name = "is_terminated", nullable = false)
    private Boolean isTerminated;

    @Basic
    @Column(name = "read_enabled", nullable = false)
    private Boolean readEnabled;

    @Basic
    @Column(name = "create_enabled", nullable = false)
    private Boolean createEnabled;

    @Basic
    @Column(name = "update_enabled", nullable = false)
    private Boolean updateEnabled;

    @Basic
    @Column(name = "delete_enabled", nullable = false)
    private Boolean deleteEnabled;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<CommentModel> comments = new ArrayList<>();


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
