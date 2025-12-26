package com.example.demo.service.impl;

import com.example.demo.entity.Zone;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.ZoneService;

import java.time.Instant;
import java.util.List;

public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneServiceImpl(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Zone createZone(Zone zone) {

        if (zone.getPriorityLevel() < 1) {
            throw new BadRequestException("priority must be >= 1");
        }

        if (zoneRepository.findByZoneName(zone.getZoneName()).isPresent()) {
            throw new BadRequestException("zone name must be unique");
        }

        zone.setActive(true);
        zone.setCreatedAt(Instant.now());
        zone.setUpdatedAt(Instant.now());

        return zoneRepository.save(zone);
    }

    @Override
    public Zone updateZone(Long id, Zone updatedZone) {

        Zone existingZone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));

        existingZone.setZoneName(updatedZone.getZoneName());
        existingZone.setPriorityLevel(updatedZone.getPriorityLevel());
        existingZone.setPopulation(updatedZone.getPopulation());
        existingZone.setUpdatedAt(Instant.now());

        return zoneRepository.save(existingZone);
    }

    @Override
    public Zone getZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
    }

    @Override
    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }
@Override
public Zone deactivateZone(Long id) {

    Zone zone = zoneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));

    zone.setActive(false);
    zone.setUpdatedAt(Instant.now());

    return zoneRepository.save(zone);
}

}
