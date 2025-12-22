package com.example.demo.service;

import com.example.demo.entity.LoadSheddingEvent;
import java.util.List;

public interface LoadSheddingService {
    LoadSheddingEvent triggerLoadShedding(Long zoneId);
    List<LoadSheddingEvent> getByZoneId(Long zoneId);
}
