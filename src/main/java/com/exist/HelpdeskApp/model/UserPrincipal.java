package com.exist.HelpdeskApp.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserPrincipal implements UserDetails {
    private final Account account;

    public UserPrincipal(Account account) {
        this.account = account;
    }

    public Integer getId() {
        return account.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getSecurityRoles().stream()
                .flatMap(role -> Stream.concat(
                    Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getName())),
                    role.getPermissions().stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                ))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
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
        return account.isEnabled();
    }
}
