package com.example.demo.service.impl;

import com.example.demo.entity.SupplyForecast;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.SupplyForecastRepository;
import com.example.demo.service.SupplyForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;


import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplyForecastServiceImpl implements SupplyForecastService {

    private final SupplyForecastRepository supplyForecastRepository;
    Timestamp now = new Timestamp(System.currentTimeMillis());


    @Override
    public SupplyForecast createForecast(SupplyForecast forecast) {
        if (forecast.getAvailableSupplyMW() < 0) {
            throw new BadRequestException("Supply must be >= 0");
        }
        if (forecast.getGeneratedAt().before(now)){
            throw new BadRequestException("Forecast start must be before end (range)");
        }
        return supplyForecastRepository.save(forecast);
    }

    @Override
    public SupplyForecast updateForecast(Long id, SupplyForecast forecast) {
        SupplyForecast existing = getForecastById(id);
        if (forecast.getAvailableSupplyMW() < 0) {
            throw new BadRequestException("Supply must be >= 0");
        }
        if (!forecast.getForecastStart().before(forecast.getForecastEnd())) {
            throw new BadRequestException("Forecast start must be before end (range)");
        }
        existing.setAvailableSupplyMW(forecast.getAvailableSupplyMW());
        existing.setForecastStart(forecast.getForecastStart());
        existing.setForecastEnd(forecast.getForecastEnd());
        return supplyForecastRepository.save(existing);
    }

    @Override
    public SupplyForecast getForecastById(Long id) {
        return supplyForecastRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast not found"));
    }

    @Override
    public SupplyForecast getLatestForecast() {
        return supplyForecastRepository.findFirstByOrderByGeneratedAtDesc()
                .orElseThrow(() -> new ResourceNotFoundException("No forecasts found"));
    }

    @Override
    public List<SupplyForecast> getAllForecasts() {
        return supplyForecastRepository.findAll();
    }
}
