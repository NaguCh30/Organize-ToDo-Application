package com.organize.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.organize.dto.request.CreateGoalRequest;
import com.organize.dto.request.UpdateGoalRequest;
import com.organize.dto.response.GoalResponse;
import com.organize.service.GoalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {
 
    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(@Valid @RequestBody CreateGoalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(goalService.createGoal(request));
    }

    @GetMapping
    public ResponseEntity<List<GoalResponse>> getGoals() {
        return ResponseEntity.status(HttpStatus.OK).body(goalService.getGoals());
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<GoalResponse> getGoalById(@PathVariable String goalId) {
        return ResponseEntity.status(HttpStatus.OK).body(goalService.getGoalById(goalId));
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<GoalResponse> updateGoal(@PathVariable String goalId, @Valid @RequestBody UpdateGoalRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(goalService.updateGoal(goalId, request));
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<String> deleteGoal(@PathVariable String goalId) {
        goalService.deleteGoal(goalId);
        return ResponseEntity.status(HttpStatus.OK).body("Goal deleted successfully");
    }
}
