package com.example.demo.service;

import com.example.demo.entity.ZoneRestorationRecord;
import java.util.List;

public interface ZoneRestorationService {
    ZoneRestorationRecord restoreZone(ZoneRestorationRecord record); // changed to return type
    ZoneRestorationRecord getRecordById(Long id);
    List<ZoneRestorationRecord> getRecordsByZoneId(Long zoneId); // exact match with impl
}
