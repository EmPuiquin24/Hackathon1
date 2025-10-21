package com.qhapaq.oreo.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[A-Za-z0-9_.]+$", message = "Username can contain letters, numbers, underscore and dot only")
    private String username;

    @Column(nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    @NotNull
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Role role;


    @Column
    private String branch;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = role == null ? "ROLE_USER" : "ROLE_" + role.name();
        return List.of(new SimpleGrantedAuthority(roleName));
    }
    
}

