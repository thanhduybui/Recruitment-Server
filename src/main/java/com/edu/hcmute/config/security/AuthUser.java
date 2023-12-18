package com.edu.hcmute.config.security;

import com.edu.hcmute.entity.AppUser;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;



@Builder
public class AuthUser implements UserDetails {

    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> roles;

    public static AuthUser create(AppUser user){
        Collection<SimpleGrantedAuthority> roles = Collections.singleton(
                new SimpleGrantedAuthority(user.getRole().toString()));

        return AuthUser.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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
