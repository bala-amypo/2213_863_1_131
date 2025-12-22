package com.example.demo.controller;

import com.example.demo.entity.ZoneRestorationRecord;
import com.example.demo.service.ZoneRestorationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restorations")
@RequiredArgsConstructor
public class ZoneRestorationController {

    private final ZoneRestorationService zoneRestorationService;

    @PostMapping("/")
    public ResponseEntity<ZoneRestorationRecord> restoreZone(@RequestBody ZoneRestorationRecord record) {
        return ResponseEntity.ok(zoneRestorationService.restoreZone(record));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoneRestorationRecord> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(zoneRestorationService.getRecordById(id));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<ZoneRestorationRecord>> getRecordsForZone(@PathVariable Long zoneId) {
        return ResponseEntity.ok(zoneRestorationService.getRecordsForZone(zoneId));
    }
}
