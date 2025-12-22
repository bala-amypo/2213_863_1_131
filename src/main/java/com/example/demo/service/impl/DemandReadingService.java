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

    private final DemandReadingRepository repo
    
    
    sitory;

    public DemandReadingServiceImpl(DemandReadingRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DemandReading> getReadingsForZone(Long zoneId) {
        return repository.findByZoneId(zoneId);
    }

    @Override
    public DemandReading getLatestReading(Long zoneId) {
        return repository.findTopByZoneIdOrderByRecordedAtDesc(zoneId);
    }
}

