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

    public ZoneRestorationServiceImpl(ZoneRestorationRecordRepository restorationRepository,
                                      LoadSheddingEventRepository eventRepository,
                                      ZoneRepository zoneRepository) {
        this.restorationRepository = restorationRepository;
        this.eventRepository = eventRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public ZoneRestorationRecord restoreZone(ZoneRestorationRecord record) {
        LoadSheddingEvent event = eventRepository.findById(record.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (record.getZone() == null || record.getZone().getId() == null) {
            record.setZone(event.getZone());
        } else {
            if (!record.getZone().getId().equals(event.getZone().getId())) {
                throw new BadRequestException("Zone in restoration record does not match Event's zone");
            }
            Zone zone = zoneRepository.findById(record.getZone().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
            record.setZone(zone);
        }

        if (record.getRestoredAt().isBefore(event.getEventStart()) || record.getRestoredAt().equals(event.getEventStart())) {
            throw new BadRequestException("after event start");
        }

        return restorationRepository.save(record);
    }

    @Override
    public ZoneRestorationRecord getRecordById(Long id) {
        return restorationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));
    }

    @Override
    public List<ZoneRestorationRecord> getRecordsForZone(Long zoneId) {
        if (!zoneRepository.existsById(zoneId)) {
            throw new ResourceNotFoundException("Zone not found");
        }
        return restorationRepository.findByZoneIdOrderByRestoredAtDesc(zoneId);
    }

    @Override
    public String restoreZone(Long zoneId) {
        return "Zone restored successfully";
    }
}
