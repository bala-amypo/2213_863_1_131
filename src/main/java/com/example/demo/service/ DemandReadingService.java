package com.example.demo.service;

import com.example.demo.entity.DemandReading;
import java.util.List;

public interface DemandReadingService {
    DemandReading create(DemandReading demandReading);
    List<DemandReading> getByZoneId(Long zoneId);
}
