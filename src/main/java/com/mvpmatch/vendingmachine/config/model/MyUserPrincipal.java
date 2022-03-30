package com.mvpmatch.vendingmachine.config.model;

import com.mvpmatch.vendingmachine.model.UserEntity;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Created by vedadsurkovic on 3/30/22
 **/
public class MyUserPrincipal extends User {

    private static final long serialVersionUID = -7131187143071107733L;

    private final UserEntity userEntity;

    public MyUserPrincipal(UserEntity userEntity) {
        super(userEntity.getUsername(),
            userEntity.getPassword(),
            Stream.of(new SimpleGrantedAuthority(
                userEntity.getRole().toString()))
                .collect(Collectors.toList()));

        this.userEntity = userEntity;
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(
            userEntity.getRole().toString()));
    }

    public UserEntity getUser() {
        return userEntity;
    }
}
