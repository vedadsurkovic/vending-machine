package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.entity.UserEntity;
import com.mvpmatch.vendingmachine.repository.ProductRepository;
import com.mvpmatch.vendingmachine.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by vedadsurkovic on 3/28/22
 **/
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(final PasswordEncoder encoder,
        final UserRepository userRepository) {

        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    public UserEntity create(final UserEntity userEntity) {

        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    public UserEntity update(final UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public Optional<UserEntity> read(final Long userId) {
        return userRepository.findById(userId);
    }

    public List<UserEntity> readAll() {
        return userRepository.findAll();
    }

    public void delete(final Long productId) {
        userRepository.findById(productId)
            .ifPresent(userRepository::delete);
    }

    public Optional<UserEntity> deposit(final Long userId, final Long deposit) {
        if ((deposit % 5) != 0)
            return Optional.empty();

        return userRepository.findById(userId).flatMap(
            user -> {
                final Long newDeposit = user.getDeposit() + deposit;
                user.setDeposit(newDeposit);
                return Optional.of(userRepository.save(user));
            });
    }

    public Optional<UserEntity> reset(final Long userId) {
        return userRepository.findById(userId).flatMap(
            user -> {
                user.setDeposit(0L);
                return Optional.of(userRepository.save(user));
            });
    }

}
