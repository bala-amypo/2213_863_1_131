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

    public ZoneServiceImpl(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Zone createZone(Zone zone) {
        if (zone.getPriorityLevel() < 1) {
            throw new BadRequestException("Priority level must be >= 1");
        }
        if (zoneRepository.findByZoneName(zone.getZoneName()).isPresent()) {
            throw new BadRequestException("Zone name must be unique");
        }
        return zoneRepository.save(zone);
    }

    @Override
    public Zone updateZone(Long id, Zone zoneDetails) {
        Zone zone = getZoneById(id);
        
        // If name changes, check uniqueness
        if (!zone.getZoneName().equals(zoneDetails.getZoneName())) {
             if (zoneRepository.findByZoneName(zoneDetails.getZoneName()).isPresent()) {
                throw new BadRequestException("Zone name must be unique");
            }
        }
        
        if (zoneDetails.getPriorityLevel() < 1) {
             throw new BadRequestException("Priority level must be >= 1");
        }

        zone.setZoneName(zoneDetails.getZoneName());
        zone.setPriorityLevel(zoneDetails.getPriorityLevel());
        zone.setPopulation(zoneDetails.getPopulation());
        // active is NOT updated here normally, unless specified. Prompt says "PUT /api/zones/{id}" implies full update? 
        // Usually full update. Let's update active too if provided.
        zone.setActive(zoneDetails.getActive()); 
        
        return zoneRepository.save(zone);
    }

    @Override
    public Zone getZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
    }

    @Override
    public List<Zone> getAllZones() {
        return zoneRepository.findByActiveTrueOrderByPriorityLevelAsc(); // Using the requirement's specific fetch method? 
        // Wait, prompt says "GET /api/zones". It doesn't strictly say it MUST use findByActiveTrue... 
        // But usually "Rules" section for Repository lists methods required.
        // Let's use findAll() for general list, or check logic. 
        // Actually, for Load Shedding logic, priority matters. For generic GET, maybe all?
        // Let's assume generic GET returns all. Logic for load shedding will use the repository method explicitly.
        // However, looking at repository methods: "findByActiveTrueOrderByPriorityLevelAsc()" is listed.
        // I'll stick to findAll() for the CRUD endpoint unless the controller specifies otherwise,
        // BUT the prompt says "Rules: ... Duplicate zoneName -> BadRequestException containing 'unique'".
        // This suggests CRUD logic.
        // I will return findAll() for getAllZones.
        return zoneRepository.findAll();
    }

    @Override
    public Zone deactivateZone(Long id) {
        Zone zone = getZoneById(id);
        zone.setActive(false);
        return zoneRepository.save(zone);
    }
}
