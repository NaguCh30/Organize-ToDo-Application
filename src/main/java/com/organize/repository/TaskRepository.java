package com.organize.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.organize.entity.Task;
import com.organize.entity.TaskStatus;

public interface TaskRepository extends MongoRepository<Task, String> {
    
    Optional<Task> findByIdAndUserId(String taskId, String userId);

    List<Task> findByGoalIdAndUserId(String goalId, String userId);

    boolean existsByGoalIdAndTitleIgnoreCase(String goalId, String title);


    //for status update and progress calculation
    long countByGoalIdAndUserId(String goalId, String userId);

    long countByGoalIdAndUserIdAndStatus(String goalId, String userId, TaskStatus status);

    //delete all tasks for aspecific goal
    void deleteByGoalIdAndUserId(String goalId, String userId);



    //Schedule methods

    List<Task> findByUserIdAndScheduledStartIsNotNull(String userId);

    List<Task> findByUserIdAndScheduledStartIsNull(String userId);


    Optional<Task> findByUserIdAndTitleIgnoreCase(String userId, String title);
}
