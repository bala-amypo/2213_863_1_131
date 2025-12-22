package com.example.demo.service.impl;

import com.example.demo.entity.DemandReading;
import com.example.demo.entity.LoadSheddingEvent;
import com.example.demo.entity.SupplyForecast;
import com.example.demo.entity.Zone;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DemandReadingRepository;
import com.example.demo.repository.LoadSheddingEventRepository;
import com.example.demo.repository.SupplyForecastRepository;
import com.example.demo.service.LoadSheddingService;
import com.example.demo.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadSheddingServiceImpl implements LoadSheddingService {

    private final LoadSheddingEventRepository loadSheddingEventRepository;
    private final DemandReadingRepository demandReadingRepository;
    private final SupplyForecastRepository supplyForecastRepository;
    private final ZoneService zoneService;

    @Override
    public LoadSheddingEvent triggerLoadShedding(Long zoneId) {
        Zone zone = zoneService.getById(zoneId);
        
        DemandReading reading = demandReadingRepository.findFirstByZoneIdOrderByRecordedAtDesc(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("No readings"));
                
        SupplyForecast forecast = supplyForecastRepository.findFirstByOrderByGeneratedAtDesc()
                .orElseThrow(() -> new ResourceNotFoundException("No forecasts"));
                
        if (reading.getDemandValue() <= forecast.getForecastedSupply()) {
            throw new BadRequestException("No overload");
        }
        
        LoadSheddingEvent event = LoadSheddingEvent.builder()
                .zone(zone)
                .eventStart(LocalDateTime.now())
                .reason("Demand " + reading.getDemandValue() + " exceeds supply " + forecast.getForecastedSupply())
                .build();
                
        return loadSheddingEventRepository.save(event);
    }

    @Override
    public List<LoadSheddingEvent> getByZoneId(Long zoneId) {
        zoneService.getById(zoneId); // check exist
        return loadSheddingEventRepository.findByZoneIdOrderByEventStartDesc(zoneId);
    }
}
