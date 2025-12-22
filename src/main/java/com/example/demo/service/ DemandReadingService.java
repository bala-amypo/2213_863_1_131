package com.example.demo.service;

import com.example.demo.entity.DemandReading;
import java.util.List;

public interface DemandReadingService {

    DemandReading createReading(DemandReading reading);

    List<DemandReading> getReadingsForZone(Long zoneId);

    DemandReading getLatestReading(Long zoneId);
}


