package com.example.backend.config;


import com.example.backend.model.Speler;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class SpelerPrincipal implements UserDetails {

    private final Speler speler;

    public SpelerPrincipal(Speler speler) {
        this.speler = speler;
    }

    public Speler getSpeler() {
        return speler;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_SPELER"));
    }

    @Override
    public String getPassword() {
        return speler.getPassword();
    }

    @Override
    public String getUsername() {
        return speler.getUsername();
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