package ru.amelin.springBoot.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.amelin.springBoot.models.User;

import java.util.Collection;
import java.util.Collections;

/**
 * Обертка над User для работы Security над ним
 */
public class UserDetailsImpl implements UserDetails {

    @Getter
    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }


    @Override
    //нужен для авторизации
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.user.getRole()));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getName();
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
