package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "zoneName"))
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zoneName;
    private Integer priorityLevel;
    private Integer population;
    private Boolean active = true;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }
}
