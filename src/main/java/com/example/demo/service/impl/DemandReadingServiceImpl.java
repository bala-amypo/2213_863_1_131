package com.example.demo.service.impl;

import com.example.demo.entity.DemandReading;
import com.example.demo.entity.Zone;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.DemandReadingRepository;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.DemandReadingService;
import com.example.demo.service.ZoneService; 
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service 
public class DemandReadingServiceImpl implements DemandReadingService {

    private final DemandReadingRepository demandReadingRepository;
    private final ZoneService zoneService;

    @Override
    public DemandReading create(DemandReading demandReading) {
        if (demandReading.getDemandValue() < 0) {
            throw new BadRequestException(">= 0");
        }
        if (demandReading.getRecordedAt().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("future");
        }
        // Ensure zone exists
        Zone zone = zoneService.getById(demandReading.getZone().getId());
        demandReading.setZone(zone);
        return demandReadingRepository.save(demandReading);
    }

    @Override
    public List<DemandReading> getByZoneId(Long zoneId) {
        zoneService.getById(zoneId); // check exists
        return demandReadingRepository.findByZoneIdOrderByRecordedAtDesc(zoneId);
    }
}
