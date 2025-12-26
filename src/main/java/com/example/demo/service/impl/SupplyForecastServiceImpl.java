package com.example.demo.service.impl;

import com.example.demo.entity.SupplyForecast;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.SupplyForecastRepository;
import com.example.demo.service.SupplyForecastService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplyForecastServiceImpl implements SupplyForecastService {

    private final SupplyForecastRepository supplyForecastRepository;

    public SupplyForecastServiceImpl(SupplyForecastRepository supplyForecastRepository) {
        this.supplyForecastRepository = supplyForecastRepository;
    }

    @Override
    public SupplyForecast createForecast(SupplyForecast forecast) {
        if (forecast.getAvailableSupplyMW() < 0) {
            throw new BadRequestException("Supply must be >= 0");
        }
        if (forecast.getForecastStart().isAfter(forecast.getForecastEnd()) || forecast.getForecastStart().equals(forecast.getForecastEnd())) {
            throw new BadRequestException("Forecast range invalid (Start must be before End)");
        }
        return supplyForecastRepository.save(forecast);
    }

    @Override
    public SupplyForecast updateForecast(Long id, SupplyForecast forecastDetails) {
        SupplyForecast forecast = getForecastById(id);
        
        if (forecastDetails.getAvailableSupplyMW() < 0) {
            throw new BadRequestException("Supply must be >= 0");
        }
        if (forecastDetails.getForecastStart().isAfter(forecastDetails.getForecastEnd())) {
             throw new BadRequestException("Forecast range invalid");
        }

        forecast.setAvailableSupplyMW(forecastDetails.getAvailableSupplyMW());
        forecast.setForecastStart(forecastDetails.getForecastStart());
        forecast.setForecastEnd(forecastDetails.getForecastEnd());
        
        return supplyForecastRepository.save(forecast);
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
