package com.example.demo.service.impl;

import com.example.demo.entity.Zone;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.ZoneService; 
import org.springframework.stereotype.Service;
import java.util.List;

@Service 
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;

    @Override
    public Zone create(Zone zone) {
        if (zoneRepository.findByZoneName(zone.getZoneName()).isPresent()) {
            throw new BadRequestException("unique");
        }
        if (zone.getPriorityLevel() < 0) {
            throw new BadRequestException(">= 0");
        }
        return zoneRepository.save(zone);
    }

    @Override
    public Zone update(Long id, Zone zone) {
        Zone existing = getById(id);
        existing.setZoneName(zone.getZoneName());
        existing.setRegion(zone.getRegion());
        existing.setPriorityLevel(zone.getPriorityLevel());
        existing.setActive(zone.getActive());
        if (existing.getPriorityLevel() < 0) {
            throw new BadRequestException(">= 0");
        }
        return zoneRepository.save(existing);
    }

    @Override
    public Zone getById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
    }

    @Override
    public List<Zone> getAll() {
        return zoneRepository.findAll();
    }

    @Override
    public void deactivate(Long id) {
        Zone zone = getById(id);
        zone.setActive(false);
        zoneRepository.save(zone);
    }
}
