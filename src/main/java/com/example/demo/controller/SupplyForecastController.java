package com.example.demo.controller;

import com.example.demo.entity.SupplyForecast;
import com.example.demo.service.SupplyForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supply-forecasts")
@RequiredArgsConstructor
public class SupplyForecastController {

    private final SupplyForecastService supplyForecastService;

    @PostMapping("/")
    public ResponseEntity<SupplyForecast> createForecast(@RequestBody SupplyForecast forecast) {
        return ResponseEntity.ok(supplyForecastService.createForecast(forecast));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplyForecast> updateForecast(@PathVariable Long id, @RequestBody SupplyForecast forecast) {
        return ResponseEntity.ok(supplyForecastService.updateForecast(id, forecast));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplyForecast> getForecastById(@PathVariable Long id) {
        return ResponseEntity.ok(supplyForecastService.getForecastById(id));
    }

    @GetMapping("/latest")
    public ResponseEntity<SupplyForecast> getLatestForecast() {
        return ResponseEntity.ok(supplyForecastService.getLatestForecast());
    }

    @GetMapping("/")
    public ResponseEntity<List<SupplyForecast>> getAllForecasts() {
        return ResponseEntity.ok(supplyForecastService.getAllForecasts());
    }
}
