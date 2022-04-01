package com.mvpmatch.vendingmachine.controller;

import com.mvpmatch.vendingmachine.entity.ProductEntity;
import com.mvpmatch.vendingmachine.service.ProductService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by vedadsurkovic on 3/28/22
 **/
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<?> create(@RequestBody final ProductEntity productEntity) {
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
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<?> update(@RequestBody final ProductEntity productEntity) {
        return ResponseEntity.ok(productService.update(productEntity));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<?> delete(@PathVariable final Long productId) {
        productService.delete(productId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/buy")
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<?> buy(@RequestBody final Map<String, String> request) {
        return ResponseEntity.ok(productService.buy(
            Long.valueOf(request.get("userId")),
            Long.valueOf(request.get("productId")),
            Integer.valueOf(request.get("amount"))));
    }
}
