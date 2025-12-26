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
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.LoadSheddingService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoadSheddingServiceImpl implements LoadSheddingService {

    private final LoadSheddingEventRepository eventRepository;
    private final SupplyForecastRepository forecastRepository;
    private final ZoneRepository zoneRepository;
    private final DemandReadingRepository demandRepository;

    public LoadSheddingServiceImpl(LoadSheddingEventRepository eventRepository,
                                   SupplyForecastRepository forecastRepository,
                                   ZoneRepository zoneRepository,
                                   DemandReadingRepository demandRepository) {
        this.eventRepository = eventRepository;
        this.forecastRepository = forecastRepository;
        this.zoneRepository = zoneRepository;
        this.demandRepository = demandRepository;
    }

    @Override
    public List<LoadSheddingEvent> triggerLoadShedding(Long forecastId) {
        SupplyForecast forecast = forecastRepository.findById(forecastId)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast not found"));

        // Calculate total demand from latest readings of active zones
        List<Zone> activeZones = zoneRepository.findByActiveTrueOrderByPriorityLevelAsc();
        if (activeZones.isEmpty()) {
            throw new BadRequestException("No suitable zones found for shedding");
        }

        double totalDemand = 0;
        List<DemandReading> currentReadings = new ArrayList<>();

        for (Zone zone : activeZones) {
            Optional<DemandReading> reading = demandRepository.findFirstByZoneIdOrderByRecordedAtDesc(zone.getId());
            if (reading.isPresent()) {
                totalDemand += reading.get().getDemandMW();
                currentReadings.add(reading.get());
            }
        }

        double availableSupply = forecast.getAvailableSupplyMW();
        if (totalDemand <= availableSupply) {
            throw new BadRequestException("No overload detected (Demand: " + totalDemand + " <= Supply: " + availableSupply + ")");
        }

        double deficit = totalDemand - availableSupply;
        List<LoadSheddingEvent> events = new ArrayList<>();
        double shedAmount = 0;

        // Shed zones in order (Priority 1 first, assuming 1 is lowest/sheddable priority based on ASC order requirement)
        for (Zone zone : activeZones) {
            if (shedAmount >= deficit) break;

            // Find current demand for this zone
            // We optimized by fetching above, but let's re-find or map. 
            // The loop above iterates same list.
            Optional<DemandReading> readingOpt = demandRepository.findFirstByZoneIdOrderByRecordedAtDesc(zone.getId());
            
            if (readingOpt.isPresent()) {
                double zoneDemand = readingOpt.get().getDemandMW();
                
                LoadSheddingEvent event = LoadSheddingEvent.builder()
                        .zone(zone)
                        .eventStart(Instant.now())
                        .eventEnd(Instant.now().plusSeconds(3600)) // Default 1 hour? Or forecast end? Let's use Forecast End.
                        .eventEnd(forecast.getForecastEnd())
                        .reason("Load shedding triggered due to supply deficit")
                        .triggeredByForecastId(forecast.getId())
                        .expectedDemandReductionMW(zoneDemand)
                        .build();

                events.add(eventRepository.save(event));
                shedAmount += zoneDemand;
            }
        }

        if (events.isEmpty()) {
             throw new BadRequestException("No suitable zones to shed (No active readings)");
        }

        return events;
    }

    @Override
    public LoadSheddingEvent getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    @Override
    public List<LoadSheddingEvent> getEventsByZoneId(Long zoneId) {
        if (!zoneRepository.existsById(zoneId)) {
             throw new ResourceNotFoundException("Zone not found");
        }
        return eventRepository.findByZoneIdOrderByEventStartDesc(zoneId);
    }

    @Override
    public List<LoadSheddingEvent> getAllEvents() {
        return eventRepository.findAll();
    }
}
