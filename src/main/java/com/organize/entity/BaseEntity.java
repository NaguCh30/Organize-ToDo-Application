package com.organize.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {
    
    @Id
    private String id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
