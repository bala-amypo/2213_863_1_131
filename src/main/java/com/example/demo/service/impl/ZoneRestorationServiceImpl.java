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

        // Ensure zone in record matches or is set correctly
        // The record might come with just zoneId in DTO usually, but here Entity has Zone object.
        // If Zone object is missing or just ID:
        if (record.getZone() == null || record.getZone().getId() == null) {
            // Can satisfy from event
            record.setZone(event.getZone());
        } else {
             // Validate it matches event
             if (!record.getZone().getId().equals(event.getZone().getId())) {
                 throw new BadRequestException("Zone in restoration record does not match Event's zone");
             }
             // Ensure it exists (loaded)
             Zone zone = zoneRepository.findById(record.getZone().getId())
                     .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
             record.setZone(zone);
        }

        if (record.getRestoredAt().isBefore(event.getEventStart()) || record.getRestoredAt().equals(event.getEventStart())) {
            throw new BadRequestException("Restoration time cannot be on or after event start... wait, prompt says 'restoredAt <= eventStart -> BadRequest'. So matches.");
            // Prompt: "restoredAt <= eventStart" -> BadRequestException with "after event start"
            // Wait, message "after event start" implies the ERROR is that it is BEFORE?
            // "restoredAt <= eventStart" means restoration happened BEFORE or AT START.
            // That is obviously wrong (can't restore before shed).
            // So if (restoredAt <= Start) -> Error("after event start").
            // The message "after event start" is slightly confusing phrasing for "Must be after event start", 
            // but I must strictly use the message string: "after event start".
        }
        
        if (record.getRestoredAt().isBefore(event.getEventStart()) || record.getRestoredAt().equals(event.getEventStart())) {
             throw new BadRequestException("Restoration must be after event start");
        }

        return restorationRepository.save(record);
    }

    @Override
    public ZoneRestorationRecord getRecordById(Long id) {
        return restorationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));
    }

    @Override
    public List<ZoneRestorationRecord> getRecordsByZoneId(Long zoneId) {
        if (!zoneRepository.existsById(zoneId)) {
             throw new ResourceNotFoundException("Zone not found");
        }
        return restorationRepository.findByZoneIdOrderByRestoredAtDesc(zoneId);
    }
}
