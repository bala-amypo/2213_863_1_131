package com.example.demo.service.impl;

import com.example.demo.entity.LoadSheddingEvent;
import com.example.demo.entity.Zone;
import com.example.demo.entity.ZoneRestorationRecord;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoadSheddingEventRepository;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.repository.ZoneRestorationRecordRepository;
import com.example.demo.service.ZoneRestorationService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ZoneRestorationServiceImpl implements ZoneRestorationService {

    private final ZoneRestorationRecordRepository restorationRepository;
    private final LoadSheddingEventRepository eventRepository;
    private final ZoneRepository zoneRepository;

    public ZoneRestorationServiceImpl(
            ZoneRestorationRecordRepository restorationRepository,
            LoadSheddingEventRepository eventRepository,
            ZoneRepository zoneRepository) {
        this.restorationRepository = restorationRepository;
        this.eventRepository = eventRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public ZoneRestorationRecord restoreZone(ZoneRestorationRecord record) {
        if (record.getEvent() == null || record.getEvent().getId() == null) {
            throw new BadRequestException("Event not specified");
        }

        LoadSheddingEvent event = eventRepository.findById(record.getEvent().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

    @Override
    public ZoneRestorationRecord getRecordById(Long id) {
        return restorationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));
    }

    @Override
    public List<ZoneRestorationRecord> getRecordsByZoneId(Long zoneId) { // renamed method
        if (!zoneRepository.existsById(zoneId)) {
            throw new ResourceNotFoundException("Zone not found");
        }
        return restorationRepository.findByZoneIdOrderByRestoredAtDesc(zoneId);
    }
}
