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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoadSheddingServiceImpl implements LoadSheddingService {

    private final LoadSheddingEventRepository eventRepository;
    private final SupplyForecastRepository forecastRepository;
    private final ZoneRepository zoneRepository;
    private final DemandReadingRepository demandRepository;

    public LoadSheddingServiceImpl(
            LoadSheddingEventRepository eventRepository,
            SupplyForecastRepository forecastRepository,
            ZoneRepository zoneRepository,
            DemandReadingRepository demandRepository) {

        this.eventRepository = eventRepository;
        this.forecastRepository = forecastRepository;
        this.zoneRepository = zoneRepository;
        this.demandRepository = demandRepository;
    }

    @Override
    public LoadSheddingEvent triggerLoadShedding(Long forecastId) {

        SupplyForecast forecast = forecastRepository.findById(forecastId)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast not found"));

        List<Zone> activeZones =
                zoneRepository.findByActiveTrueOrderByPriorityLevelAsc();

        if (activeZones.isEmpty()) {
            throw new BadRequestException("No active zones available");
        }

        double totalDemand = 0;

        for (Zone zone : activeZones) {
            Optional<DemandReading> reading =
                    demandRepository.findFirstByZoneIdOrderByRecordedAtDesc(zone.getId());
            if (reading.isPresent()) {
                totalDemand += reading.get().getDemandMW();
            }
        }

        if (totalDemand <= forecast.getAvailableSupplyMW()) {
            throw new BadRequestException("No overload detected");
        }

        for (Zone zone : activeZones) {

            Optional<DemandReading> readingOpt =
                    demandRepository.findFirstByZoneIdOrderByRecordedAtDesc(zone.getId());

            if (readingOpt.isPresent()) {

                LoadSheddingEvent event = new LoadSheddingEvent();
                event.setZone(zone);
                event.setEventStart(LocalDateTime.now());
                event.setReason("AUTO_LOAD_SHEDDING");

                return eventRepository.save(event);
            }
        }

        throw new BadRequestException("Unable to perform load shedding");
    }

    @Override
    public LoadSheddingEvent getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    @Override
    public List<LoadSheddingEvent> getEventsByZoneId(Long zoneId) {
        return eventRepository.findByZoneIdOrderByEventStartDesc(zoneId);
    }

    @Override
    public List<LoadSheddingEvent> getAllEvents() {
        return eventRepository.findAll();
    }
}
