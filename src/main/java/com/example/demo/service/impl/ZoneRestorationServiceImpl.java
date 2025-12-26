package com.example.demo.service.impl;

import com.example.demo.entity.ZoneRestorationRecord;
import com.example.demo.repository.ZoneRestorationRecordRepository;
import com.example.demo.service.ZoneRestorationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


@Service
@RequiredArgsConstructor
public class ZoneRestorationServiceImpl implements ZoneRestorationService {

    private final ZoneRestorationRecordRepository repository;

    @Override
    public void restoreZone(ZoneRestorationRecord record) {
        repository.save(record);
    }

    @Override
    public List<ZoneRestorationRecord> getRecordsByZoneId(Long zoneId) {
        return repository.findByZoneId(zoneId);
    }
}
