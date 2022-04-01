package com.mvpmatch.vendingmachine.config.service;

import com.mvpmatch.vendingmachine.config.model.*;
import com.mvpmatch.vendingmachine.entity.UserEntity;
import com.mvpmatch.vendingmachine.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by vedadsurkovic on 3/30/22
 **/
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new MyUserPrincipal(userEntity);
    }
}
