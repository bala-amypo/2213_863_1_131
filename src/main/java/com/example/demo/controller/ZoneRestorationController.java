package com.example.demo.controller;

import com.example.demo.entity.ZoneRestorationRecord;
import com.example.demo.service.ZoneRestorationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restorations")
public class ZoneRestorationController {

    private final ZoneRestorationService zoneRestorationService;

    public ZoneRestorationController(ZoneRestorationService zoneRestorationService) {
        this.zoneRestorationService = zoneRestorationService;
    }

    // Create / restore a zone
    @PostMapping
    public ResponseEntity<ZoneRestorationRecord> restoreZone(@RequestBody ZoneRestorationRecord record) {
        return ResponseEntity.ok(zoneRestorationService.restoreZone(record));
    }

    // Get a restoration record by ID
    @GetMapping("/{id}")
    public ResponseEntity<ZoneRestorationRecord> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(zoneRestorationService.getRecordById(id));
    }

    // Get all restoration records for a specific zone
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<ZoneRestorationRecord>> getRecordsByZone(@PathVariable Long zoneId) {
        return ResponseEntity.ok(zoneRestorationService.getRecordsByZoneId(zoneId));
    }
}
