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

@Service
public class DemandReadingServiceImpl implements DemandReadingService {

    private final DemandReadingRepository demandReadingRepository;
    private final ZoneRepository zoneRepository;

    public DemandReadingServiceImpl(
            DemandReadingRepository demandReadingRepository,
            ZoneRepository zoneRepository) {

        this.demandReadingRepository = demandReadingRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public DemandReading recordReading(DemandReading reading) {

        if (reading.getDemandMW() < 0) {
            throw new BadRequestException("Demand must be non-negative");
        }

        if (reading.getRecordedAt() == null ||
            reading.getRecordedAt().isAfter(Instant.now())) {
            throw new BadRequestException("Invalid recorded time");
        }

        if (reading.getZone() == null || reading.getZone().getId() == null) {
            throw new ResourceNotFoundException("Zone not specified");
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

        return demandReadingRepository
                .findByZoneIdOrderByRecordedAtDesc(zoneId);
    }
}
