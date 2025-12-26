package com.example.demo.service.impl;

import com.example.demo.entity.DemandReading;
import com.example.demo.repository.DemandReadingRepository;
import com.example.demo.service.DemandReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandReadingServiceImpl implements DemandReadingService {

    private final DemandReadingRepository repository;

    @Override
    public List<DemandReading> getAllReadings() {
        return repository.findAll();
    }

    @Override
    public void saveReading(DemandReading reading) {
        repository.save(reading);
    }
}
