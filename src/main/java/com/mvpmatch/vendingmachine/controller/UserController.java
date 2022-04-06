package com.mvpmatch.vendingmachine.controller;

import com.mvpmatch.vendingmachine.config.model.MyUserPrincipal;
import com.mvpmatch.vendingmachine.entity.Role;
import com.mvpmatch.vendingmachine.entity.UserEntity;
import com.mvpmatch.vendingmachine.service.UserService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mvpmatch.vendingmachine.util.Util.checkIfRoleIsInvalid;

/**
 * Created by vedadsurkovic on 3/28/22
 **/
@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> create(@RequestBody final UserEntity userEntity) {
        return ResponseEntity.ok(userService.create(userEntity));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> get(@PathVariable final Long userId) {
        return ResponseEntity.ok(userService.read(userId));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userService.readAll());
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody final UserEntity userEntity) {
        return ResponseEntity.ok(userService.update(userEntity));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable final Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody final Map<String, String> request) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final MyUserPrincipal myUserPrincipal = (MyUserPrincipal) auth.getPrincipal();

        if (checkIfRoleIsInvalid(myUserPrincipal, Role.BUYER))
            return ResponseEntity.badRequest().body("Only user with role BUYER can access this endpoint");

        return userService.deposit(
            myUserPrincipal.getUser().getId(),
            Long.valueOf(request.get("deposit")));
    }

    @PatchMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody final Map<String, String> request) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final MyUserPrincipal myUserPrincipal = (MyUserPrincipal) auth.getPrincipal();

        if (checkIfRoleIsInvalid(myUserPrincipal, Role.BUYER))
            return ResponseEntity.badRequest().body("Only user with role BUYER can access this endpoint");

        return ResponseEntity.ok(userService.reset(
            Long.valueOf(request.get("userId"))));
    }
}
