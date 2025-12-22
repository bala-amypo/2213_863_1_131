package com.example.demo.controller;

import com.example.demo.entity.SupplyForecast;
import com.example.demo.service.SupplyForecastService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/supply-forecasts")
@Tag(name = "Supply Forecasts")
public class SupplyForecastController {

    private final SupplyForecastService service;

    public SupplyForecastController(SupplyForecastService service) {
        this.service = service;
    }

    @PostMapping
    public SupplyForecast create(@RequestBody SupplyForecast forecast) {
        return service.createForecast(forecast);
    }

    @GetMapping("/latest")
    public SupplyForecast latest() {
        return service.getLatestForecast();
    }

    @GetMapping
    public List<SupplyForecast> getAll() {
        return service.getAllForecasts();
    }
}
