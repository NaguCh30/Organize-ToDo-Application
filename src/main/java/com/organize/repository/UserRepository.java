package com.organize.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.organize.entity.User;

public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);


}
