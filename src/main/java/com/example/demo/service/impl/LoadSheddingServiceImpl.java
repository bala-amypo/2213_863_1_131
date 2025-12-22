package com.example.demo.service.impl;

import com.example.demo.entity.DemandReading;
import com.example.demo.entity.LoadSheddingEvent;
import com.example.demo.entity.SupplyForecast;
import com.example.demo.entity.Zone;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DemandReadingRepository;
import com.example.demo.repository.LoadSheddingEventRepository;
import com.example.demo.repository.SupplyForecastRepository;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.LoadSheddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadSheddingServiceImpl implements LoadSheddingService {

    private final SupplyForecastRepository supplyForecastRepository;
    private final ZoneRepository zoneRepository;
    private final DemandReadingRepository demandReadingRepository;
    private final LoadSheddingEventRepository loadSheddingEventRepository;

    @Override
    public LoadSheddingEvent triggerLoadShedding(Long forecastId) {
        SupplyForecast forecast = supplyForecastRepository.findById(forecastId)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast not found"));
        
        List<Zone> activeZones = zoneRepository.findByActiveTrueOrderByPriorityLevelAsc();
        if (activeZones.isEmpty()) {
            throw new ResourceNotFoundException("No active zones found to shed");
        }
        
        Zone targetZone = activeZones.get(0);
        
        DemandReading latestReading = demandReadingRepository.findFirstByZoneIdOrderByRecordedAtDesc(targetZone.getId())
                .orElse(null);
        
        Double reduction = latestReading != null ? latestReading.getDemandMW() : 0.0;
        
        LoadSheddingEvent event = new LoadSheddingEvent();
        event.setZone(targetZone);
        event.setEventStart(Instant.now());
        event.setEventEnd(Instant.now().plusSeconds(3600));
        event.setReason("Triggered by forecast shortfall");
        event.setTriggeredByForecastId(forecast.getId());
        event.setExpectedDemandReductionMW(reduction);
                
        return loadSheddingEventRepository.save(event);
    }

    @Override
    public LoadSheddingEvent getEventById(Long id) {
        return loadSheddingEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    @Override
    public List<LoadSheddingEvent> getEventsForZone(Long zoneId) {
        return loadSheddingEventRepository.findByZoneIdOrderByEventStartDesc(zoneId);
    }

    @Override
    public List<LoadSheddingEvent> getAllEvents() {
        return loadSheddingEventRepository.findAll();
    }
}
