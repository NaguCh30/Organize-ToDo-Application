package com.organize.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.organize.entity.Goal;

public interface GoalRepository extends MongoRepository<Goal, String> {
    
    List<Goal> findByUserId(String userId);

    Optional<Goal> findByIdAndUserId(String goalId, String userId);

    boolean existsByUserIdAndTitleIgnoreCase(String userId, String title);
}
