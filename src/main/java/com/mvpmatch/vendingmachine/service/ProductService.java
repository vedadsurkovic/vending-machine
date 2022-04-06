package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.entity.ProductEntity;
import com.mvpmatch.vendingmachine.entity.UserEntity;
import com.mvpmatch.vendingmachine.repository.ProductRepository;
import com.mvpmatch.vendingmachine.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ProductEntity create(final ProductEntity productEntity, final UserEntity user) {
        productEntity.setSeller(user);
        return productRepository.save(productEntity);
    }

    public ProductEntity update(final ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    public Optional<ProductEntity> read(final Long productId) {
        return productRepository.findById(productId);
    }

    public List<ProductEntity> readAll() {
        return productRepository.findAll();
    }

    public void delete(final Long productId) {
        productRepository.findById(productId)
            .ifPresent(productRepository::delete);
    }

    @Transactional
    public ResponseEntity<?> buy(final Long userId, final Long productId, final Integer amount) {
        final Map<String, Object> response = new HashMap<>();

        final UserEntity user = userRepository.findById(userId).orElse(null);

        if (user == null)
            return ResponseEntity.badRequest().body("User does not exist");
        if (user.getDeposit() < 5)
            return ResponseEntity.badRequest().body("Deposit is not the power of 5.");

        final ProductEntity product = productRepository.findById(productId).orElse(null);
        if (product == null)
            return ResponseEntity.badRequest().body("Product does not exist");
        if (product.getAmountAvailable() < amount)
            return ResponseEntity.badRequest().body("There is not enough product on stock.");

        final Long totalSpent = product.getCost() * amount;
        if (totalSpent > user.getDeposit())
            return ResponseEntity.badRequest().body("User does not have enough deposit");

        response.put("product", product.getProductName());
        response.put("totalSpent", totalSpent);

        product.setAmountAvailable(product.getAmountAvailable() - amount);
        productRepository.save(product);

        final Long newDeposit = user.getDeposit() - totalSpent;
        user.setDeposit(newDeposit);
        userRepository.save(user);

        response.put("change", calculateChange(newDeposit));
        return ResponseEntity.ok(response);
    }

    private Map<String, Long> calculateChange(final Long deposit) {
        final Map<String, Long> change = new HashMap<>();

        long coinNumber;
        Long newDeposit = deposit;
        if (newDeposit >= 100) {
            coinNumber = newDeposit/100;
            change.put("100", coinNumber);
            newDeposit -= 100*coinNumber;
        }
        if (newDeposit >= 50) {
            coinNumber = newDeposit/50;
            change.put("50", coinNumber);
            newDeposit -= 50*coinNumber;
        }
        if (newDeposit >= 20) {
            coinNumber = newDeposit/20;
            change.put("20", coinNumber);
            newDeposit -= 20*coinNumber;
        }
        if (newDeposit >= 10) {
            coinNumber = newDeposit/10;
            change.put("10", coinNumber);
            newDeposit -= 10*coinNumber;
        }
        if (newDeposit >= 5) {
            coinNumber = newDeposit/5;
            change.put("5", coinNumber);
        }

        return change;
    }
}
