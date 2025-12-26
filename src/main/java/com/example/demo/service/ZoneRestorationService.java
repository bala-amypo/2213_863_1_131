package com.example.demo.service;

import com.example.demo.entity.ZoneRestorationRecord;

import java.util.List;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public interface ZoneRestorationService {
    void restoreZone(ZoneRestorationRecord record);
    List<ZoneRestorationRecord> getRecordsByZoneId(Long zoneId);
}
