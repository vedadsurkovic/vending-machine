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

                response.put("change", calculateChange(totalSpent));

                return response;
            });
        });
    }

    private Map<String, Long> calculateChange(final Long total) {
        final Map<String, Long> change = new HashMap<>();
        Long spent = total;
        change.put("100", spent %100);
        if ((spent %100) > 0)
            spent -= 100 * (spent %100);
        change.put("50", spent %50);
        if ((spent %50) > 0)
            spent -= 50 * (spent %50);
        change.put("20", spent %20);
        if ((spent %20) > 0)
            spent -= 20 * (spent %20);
        change.put("10", spent %10);
        if ((spent %10) > 0)
            spent -= 10 * (spent %10);
        change.put("100", spent %100);
        if ((spent %5) > 0)
            spent -= 5 * (spent %5);

        return change;
    }
}
