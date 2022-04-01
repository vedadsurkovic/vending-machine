package com.mvpmatch.vendingmachine.controller;

import com.mvpmatch.vendingmachine.config.JwtUtils;
import com.mvpmatch.vendingmachine.config.model.*;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vedadsurkovic on 4/1/22
 **/
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(final AuthenticationManager authenticationManager, final JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<?> authenticateUser(@RequestBody final Map<String, String> authModel) {
        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authModel.get("username"), authModel.get("password")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = jwtUtils.generateJwtToken(authentication);
        final MyUserPrincipal userDetails = (MyUserPrincipal) authentication.getPrincipal();

        final Map<String, Object> response = new HashMap<>();
        response.put("jwt", jwt);
        response.put("user", userDetails);

        return ResponseEntity.ok(response);
    }
}
