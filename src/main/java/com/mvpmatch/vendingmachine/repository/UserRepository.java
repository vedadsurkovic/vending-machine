package com.mvpmatch.vendingmachine.repository;

import com.mvpmatch.vendingmachine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
