package com.mvpmatch.vendingmachine.controller;

import com.mvpmatch.vendingmachine.config.model.MyUserPrincipal;
import com.mvpmatch.vendingmachine.entity.ProductEntity;
import com.mvpmatch.vendingmachine.entity.Role;
import com.mvpmatch.vendingmachine.service.ProductService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.mvpmatch.vendingmachine.util.Util.checkIfRoleIsValid;

/**
 * Created by vedadsurkovic on 3/28/22
 **/
@RestController
@RequestMapping("/product")
@PreAuthorize("isAuthenticated()")
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final ProductEntity productEntity) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final MyUserPrincipal myUserPrincipal = (MyUserPrincipal) auth.getPrincipal();

        if (checkIfRoleIsValid(myUserPrincipal, Role.SELLER))
            return ResponseEntity.badRequest().body("Only user with role SELLER can access this endpoint");

        return ResponseEntity.ok(productService.create(productEntity));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> get(@PathVariable final Long productId) {
        return ResponseEntity.ok(productService.read(productId));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(productService.readAll());
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody final ProductEntity productEntity) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final MyUserPrincipal myUserPrincipal = (MyUserPrincipal) auth.getPrincipal();

        if (checkIfRoleIsValid(myUserPrincipal, Role.SELLER))
            return ResponseEntity.badRequest().body("Only user with role SELLER can access this endpoint");

        return ResponseEntity.ok(productService.update(productEntity));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> delete(@PathVariable final Long productId) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final MyUserPrincipal myUserPrincipal = (MyUserPrincipal) auth.getPrincipal();

        if (checkIfRoleIsValid(myUserPrincipal, Role.SELLER))
            return ResponseEntity.badRequest().body("Only user with role SELLER can access this endpoint");

        productService.delete(productId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/buy")
    public ResponseEntity<?> buy(@RequestBody final Map<String, String> request) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final MyUserPrincipal myUserPrincipal = (MyUserPrincipal) auth.getPrincipal();

        if (checkIfRoleIsValid(myUserPrincipal, Role.BUYER))
            return ResponseEntity.badRequest().body("Only user with role BUYER can access this endpoint");

        return ResponseEntity.ok(productService.buy(
            myUserPrincipal.getUser().getId(),
            Long.valueOf(request.get("productId")),
            Integer.valueOf(request.get("amount"))));
    }
}
