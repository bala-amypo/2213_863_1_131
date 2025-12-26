package com.example.demo.service;

import com.example.demo.entity.ZoneRestorationRecord;
import java.util.List;
public interface ZoneRestorationService {
    void restoreZone(ZoneRestorationRecord record);
    ZoneRestorationRecord getRecordById(Long id);
    List<ZoneRestorationRecord> getRecordsByZoneId(Long zoneId);
}
