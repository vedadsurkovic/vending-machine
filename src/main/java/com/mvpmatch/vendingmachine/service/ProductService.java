package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.model.Product;
import com.mvpmatch.vendingmachine.model.User;
import com.mvpmatch.vendingmachine.repository.ProductRepository;
import com.mvpmatch.vendingmachine.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Created by vedadsurkovic on 3/28/22
 **/
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(final ProductRepository productRepository,
        final UserRepository userRepository) {

        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Product create(final Product product) {
        return productRepository.save(product);
    }

    public Product update(final Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> read(final Long productId) {
        return productRepository.findById(productId);
    }

    public List<Product> readAll() {
        return productRepository.findAll();
    }

    public void delete(final Long productId) {
        productRepository.findById(productId)
            .ifPresent(productRepository::delete);
    }

    public Optional<?> buy(final Long userId, final Long productId, final Integer amount) {
        final Map<String, Object> response = new HashMap<>();

        return userRepository.findById(userId).map(user -> {
            if (user.getDeposit() < 5)
                return Optional.empty();
            return productRepository.findById(productId).map(product -> {
                if (product.getAmountAvailable() < amount)
                    return Optional.empty();

                response.put("product", product.getProductName());
                product.setAmountAvailable(product.getAmountAvailable() - amount);
                productRepository.save(product);

                final Long totalSpent = product.getCost() * amount;
                response.put("totalSpent", totalSpent);
                user.setDeposit(user.getDeposit() - totalSpent);
                userRepository.save(user);

                final Map<String, Long> change = new HashMap<>();
                response.put("change", change);

                return response;
            });
        });
    }
}
