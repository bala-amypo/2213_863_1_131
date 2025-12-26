package com.example.demo.service.impl;

import com.example.demo.entity.DemandReading;
import com.example.demo.entity.Zone;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DemandReadingRepository;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.DemandReadingService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandReadingServiceImpl implements DemandReadingService {

    private final DemandReadingRepository demandReadingRepository;
    private final ZoneRepository zoneRepository;

    public DemandReadingServiceImpl(DemandReadingRepository demandReadingRepository, ZoneRepository zoneRepository) {
        this.demandReadingRepository = demandReadingRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public DemandReading recordReading(DemandReading reading) {
        if (reading.getDemandMW() < 0) {
            throw new BadRequestException("Demand must be >= 0");
        }
        if (reading.getRecordedAt() == null || reading.getRecordedAt().isAfter(Instant.now())) {
            throw new BadRequestException("Recorded time cannot be in the future");
        }
        
        // Reading DTO/Entity usually just has ID for zone, but JPA entity needs Zone object.
        // We need to fetch Zone. Assuming the controller sets the Zone object or ID is available.
        // If the `reading` object passed here has a Zone with just ID:
        if (reading.getZone() == null || reading.getZone().getId() == null) {
             throw new BadRequestException("Zone ID is required");
        }
        
        Zone zone = zoneRepository.findById(reading.getZone().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        reading.setZone(zone);

        return demandReadingRepository.save(reading);
    }

    @Override
    public List<DemandReading> getReadingsByZoneId(Long zoneId) {
        if (!zoneRepository.existsById(zoneId)) {
            throw new ResourceNotFoundException("Zone not found");
        }
        return demandReadingRepository.findByZoneIdOrderByRecordedAtDesc(zoneId);
    }

    @Override
    public DemandReading getLatestReading(Long zoneId) {
        if (!zoneRepository.existsById(zoneId)) {
            throw new ResourceNotFoundException("Zone not found");
        }
        return demandReadingRepository.findFirstByZoneIdOrderByRecordedAtDesc(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("No readings found for this zone"));
    }

    @Override
    public List<DemandReading> getRecentReadings(Long zoneId, int limit) {
         if (!zoneRepository.existsById(zoneId)) {
            throw new ResourceNotFoundException("Zone not found");
        }
        // Using the repo method which orders by desc, then limit in stream
        return demandReadingRepository.findByZoneIdOrderByRecordedAtDesc(zoneId).stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
