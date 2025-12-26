package com.example.demo.controller;

import com.example.demo.dto.ZoneDTO;
import com.example.demo.entity.Zone;
import com.example.demo.service.ZoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping
    public ResponseEntity<ZoneDTO> createZone(@RequestBody ZoneDTO zoneDTO) {
        Zone zone = Zone.builder()
                .zoneName(zoneDTO.getZoneName())
                .priorityLevel(zoneDTO.getPriorityLevel())
                .population(zoneDTO.getPopulation())
                .active(zoneDTO.getActive() != null ? zoneDTO.getActive() : true)
                .build();
        Zone created = zoneService.createZone(zone);
        return ResponseEntity.ok(convertToDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZoneDTO> updateZone(@PathVariable Long id, @RequestBody ZoneDTO zoneDTO) {
        Zone zone = Zone.builder()
                .zoneName(zoneDTO.getZoneName())
                .priorityLevel(zoneDTO.getPriorityLevel())
                .population(zoneDTO.getPopulation())
                .active(zoneDTO.getActive())
                .build();
        Zone updated = zoneService.updateZone(id, zone);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoneDTO> getZoneById(@PathVariable Long id) {
        Zone zone = zoneService.getZoneById(id);
        return ResponseEntity.ok(convertToDTO(zone));
    }

    @GetMapping
    public ResponseEntity<List<ZoneDTO>> getAllZones() {
        return ResponseEntity.ok(zoneService.getAllZones().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ZoneDTO> deactivateZone(@PathVariable Long id) {
        Zone zone = zoneService.deactivateZone(id);
        return ResponseEntity.ok(convertToDTO(zone));
    }

    private ZoneDTO convertToDTO(Zone zone) {
        return ZoneDTO.builder()
                .id(zone.getId())
                .zoneName(zone.getZoneName())
                .priorityLevel(zone.getPriorityLevel())
                .population(zone.getPopulation())
                .active(zone.getActive())
                .createdAt(zone.getCreatedAt())
                .updatedAt(zone.getUpdatedAt())
                .build();
    }
}
