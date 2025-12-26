package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDTO {
    private Long id;
    private String zoneName;
    private Integer priorityLevel;
    private Integer population;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
