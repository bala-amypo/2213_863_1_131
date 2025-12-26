package com.example.demo.service;

import com.example.demo.entity.DemandReading;
import java.util.List;

public interface DemandReadingService {
    DemandReading recordReading(DemandReading reading);
    List<DemandReading> getReadingsByZoneId(Long zoneId);
    DemandReading getLatestReading(Long zoneId);
    List<DemandReading> getRecentReadings(Long zoneId, int limit);
}
