package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class ZoneDTO {
    private Long id;
    private String zoneName;
    private int priorityLevel;
    private int population;
    private boolean active;
}
