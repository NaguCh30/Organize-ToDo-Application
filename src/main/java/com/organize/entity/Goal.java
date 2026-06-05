package com.organize.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "goals")
public class Goal extends BaseEntity {

     private String userId;
    
    private String title;

    private String description;

    private GoalStatus status;
}
