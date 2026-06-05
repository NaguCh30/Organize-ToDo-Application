package com.organize.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.organize.dto.request.CreateGoalRequest;
import com.organize.dto.request.UpdateGoalRequest;
import com.organize.dto.response.GoalResponse;
import com.organize.entity.Goal;
import com.organize.entity.GoalStatus;
import com.organize.exception.DuplicateGoalException;
import com.organize.exception.GoalNotFoundException;
import com.organize.repository.GoalRepository;
import com.organize.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoalService {
    
    private final GoalRepository goalRepository;

    private String getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser().getId();
        
    }

    private String normalizeTitle(String title) {
        title = title.strip();
        title = title.replaceAll("\\s+", " ");
        return title;
    }

    private GoalResponse mapToResponse(Goal goal) {
        GoalResponse response = new GoalResponse();

        response.setId(goal.getId());
        response.setTitle(goal.getTitle());
        response.setDescription(goal.getDescription());
        response.setStatus(goal.getStatus());
        response.setCreatedAt(goal.getCreatedAt());
        response.setUpdatedAt(goal.getUpdatedAt());

        return response;
    }

    public GoalResponse createGoal(CreateGoalRequest request) {

        String userId = getCurrentUserId();

        String normalizedTitle = normalizeTitle(request.getTitle());

        if(goalRepository.existsByUserIdAndTitleIgnoreCase(userId, normalizedTitle)) {
            
            throw new DuplicateGoalException("Looks like title '" + request.getTitle() + "' used for another goal. Try different title or delete that goal.");
        }

        Goal goal = new Goal();

        LocalDateTime now = LocalDateTime.now();
        goal.setUserId(userId);
        goal.setTitle(normalizedTitle);

        String description = request.getDescription();
        if(description != null) description = description.strip();

        goal.setDescription(description);
        goal.setStatus(GoalStatus.NOT_STARTED);
        goal.setCreatedAt(now);
        goal.setUpdatedAt(now);

        Goal savedGoal = goalRepository.save(goal);

        return mapToResponse(savedGoal);
    }

    public List<GoalResponse> getGoals() {

        String userId = getCurrentUserId();

        List<Goal> goals =  goalRepository.findByUserId(userId);

        return goals.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public GoalResponse getGoalById(String goalId) {
        String userId = getCurrentUserId();

        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                                    .orElseThrow(
                                        () -> new GoalNotFoundException("Goal not found")
                                    );
        
        return mapToResponse(goal);
    }

    public GoalResponse updateGoal(String goalId, UpdateGoalRequest request) {

        String userId = getCurrentUserId();

        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                                    .orElseThrow(
                                        () -> new GoalNotFoundException("Goal not found")
                                    );
                    
        String normalizedTitle = normalizeTitle(request.getTitle());

        if(!goal.getTitle().equalsIgnoreCase(normalizedTitle)
                    && goalRepository.existsByUserIdAndTitleIgnoreCase(userId, normalizedTitle)) {
                        
                        throw new DuplicateGoalException("Goal title already exists");
        }

        goal.setTitle(normalizedTitle);

        String description = request.getDescription();

        if(description != null) description = description.strip();

        goal.setDescription(description);

        goal.setUpdatedAt(LocalDateTime.now());

        Goal updatedGoal = goalRepository.save(goal);

        return mapToResponse(updatedGoal);
    } 
    
    public void deleteGoal(String goalId) {
        String userId = getCurrentUserId();

        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                                    .orElseThrow(
                                        () -> new GoalNotFoundException("Goal not found")
                                    );
                    
        //Phase 3
        //Delete all tasks with associated with this goal
        //before deleting the goal itsef
        
        goalRepository.delete(goal);
    }

}
