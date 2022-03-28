package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.model.User;
import com.mvpmatch.vendingmachine.repository.ProductRepository;
import com.mvpmatch.vendingmachine.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Created by vedadsurkovic on 3/28/22
 **/
@Service
public class UserService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public UserService(final ProductRepository productRepository,
        final UserRepository userRepository) {

        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public User create(final User user) {
        return userRepository.save(user);
    }

    public User update(final User user) {
        return userRepository.save(user);
    }

    public Optional<User> read(final Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> readAll() {
        return userRepository.findAll();
    }

    public void delete(final Long productId) {
        userRepository.findById(productId)
            .ifPresent(userRepository::delete);
    }

    public Optional<User> deposit(final Long userId, final Long deposit) {
        if ((deposit % 5) != 0)
            return Optional.empty();

        return userRepository.findById(userId).flatMap(
            user -> {
                user.setDeposit(deposit);
                return Optional.of(userRepository.save(user));
            });
    }

    public Optional<User> reset(final Long userId) {
        return userRepository.findById(userId).flatMap(
            user -> {
                user.setDeposit(0L);
                return Optional.of(userRepository.save(user));
            });
    }

}
