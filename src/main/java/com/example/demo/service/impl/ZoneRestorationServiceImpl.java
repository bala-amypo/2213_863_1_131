package com.example.demo.service.impl;

import com.example.demo.entity.LoadSheddingEvent;
import com.example.demo.entity.Zone;
import com.example.demo.entity.ZoneRestorationRecord;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoadSheddingEventRepository;
import com.example.demo.repository.ZoneRestorationRecordRepository;
import com.example.demo.service.ZoneRestorationService;
import com.example.demo.service.ZoneService; 
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service 
public class ZoneRestorationServiceImpl implements ZoneRestorationService {

    private final ZoneRestorationRecordRepository restorationRepository;
    private final LoadSheddingEventRepository eventRepository;
    private final ZoneService zoneService;

    @Override
    public ZoneRestorationRecord restoreZone(Long zoneId) {
        Zone zone = zoneService.getById(zoneId);
        
        List<LoadSheddingEvent> events = eventRepository.findByZoneIdOrderByEventStartDesc(zoneId);
        if (events.isEmpty()) {
            throw new ResourceNotFoundException("Event not found");
        }
        
        LoadSheddingEvent latestEvent = events.get(0);
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isBefore(latestEvent.getEventStart())) {
            throw new BadRequestException("after event start");
        }
        
        latestEvent.setEventEnd(now);
        eventRepository.save(latestEvent);
        
        ZoneRestorationRecord record = ZoneRestorationRecord.builder()
                .zone(zone)
                .restoredAt(now)
                .notes("Restored automatically")
                .build();
                
        return restorationRepository.save(record);
    }

    @Override
    public List<ZoneRestorationRecord> getByZoneId(Long zoneId) {
        zoneService.getById(zoneId);
        return restorationRepository.findByZoneIdOrderByRestoredAtDesc(zoneId);
    }
}
