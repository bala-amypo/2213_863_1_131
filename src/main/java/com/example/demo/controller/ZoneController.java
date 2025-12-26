package com.example.demo.controller;

import com.example.demo.dto.ZoneDTO;
import com.example.demo.entity.Zone;
import com.example.demo.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@RestController
@RequestMapping("/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @PostMapping
    public ZoneDTO createZone(@RequestBody ZoneDTO zoneDTO) {
        Zone zone = Zone.builder()
                .zoneName(zoneDTO.getZoneName())
                .priorityLevel(zoneDTO.getPriorityLevel())
                .population(zoneDTO.getPopulation())
                .active(zoneDTO.isActive())
                .build();
        zoneService.save(zone);
        return ZoneDTO.builder()
                .id(zone.getId())
                .zoneName(zone.getZoneName())
                .priorityLevel(zone.getPriorityLevel())
                .population(zone.getPopulation())
                .active(zone.isActive())
                .build();
    }

    @GetMapping
    public List<ZoneDTO> getAllZones() {
        return zoneService.getAllZones()
                .stream()
                .map(zone -> ZoneDTO.builder()
                        .id(zone.getId())
                        .zoneName(zone.getZoneName())
                        .priorityLevel(zone.getPriorityLevel())
                        .population(zone.getPopulation())
                        .active(zone.isActive())
                        .build())
                .collect(Collectors.toList());
    }
}
