package com.example.demo.service.impl;

import com.example.demo.entity.SupplyForecast;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.SupplyForecastRepository;
import com.example.demo.service.SupplyForecastService; 
import org.springframework.stereotype.Service;

@Service 
public class SupplyForecastServiceImpl implements SupplyForecastService {

    private final SupplyForecastRepository supplyForecastRepository;

    @Override
    public SupplyForecast create(SupplyForecast supplyForecast) {
        return supplyForecastRepository.save(supplyForecast);
    }

    @Override
    public SupplyForecast getLatest() {
        return supplyForecastRepository.findFirstByOrderByGeneratedAtDesc()
                .orElseThrow(() -> new ResourceNotFoundException("No forecasts"));
    }
}
