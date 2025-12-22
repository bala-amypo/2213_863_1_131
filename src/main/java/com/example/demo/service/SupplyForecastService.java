package com.example.demo.service;

import com.example.demo.entity.SupplyForecast;

public interface SupplyForecastService {
    SupplyForecast create(SupplyForecast supplyForecast);
    SupplyForecast getLatest();
}
