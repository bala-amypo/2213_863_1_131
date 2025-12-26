package com.example.demo.service;

import com.example.demo.entity.ZoneRestorationRecord;
import java.util.List;

public interface ZoneRestorationService {

    List<ZoneRestorationRecord> getRecordsByZoneId(Long zoneId);
}
