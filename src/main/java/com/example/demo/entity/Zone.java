package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zoneName;
    private int priorityLevel;
    private int population;
    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
