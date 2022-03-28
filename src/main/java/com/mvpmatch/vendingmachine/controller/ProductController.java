package com.mvpmatch.vendingmachine.controller;

import com.mvpmatch.vendingmachine.model.Product;
import com.mvpmatch.vendingmachine.service.ProductService;
import javax.servlet.ServletRequest;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> get(final Product product) {
        return ResponseEntity.ok(productService.create(product));
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
    public ResponseEntity<?> update(final Product product) {
        return ResponseEntity.ok(productService.update(product));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> delete(@PathVariable final Long productId) {
        productService.delete(productId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/buy")
    public ResponseEntity<?> buy(ServletRequest request) {
        return ResponseEntity.ok(productService.buy(
            Long.valueOf(request.getParameter("userId")),
            Long.valueOf(request.getParameter("productId")),
            Integer.valueOf(request.getParameter("amount"))));
    }
}
