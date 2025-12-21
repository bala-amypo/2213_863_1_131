package com.example.demo.controller;

import com.example.demo.service.LoadSheddingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/load-shedding")
@Tag(name = "Load Shedding")
public class LoadSheddingController {

    private final LoadSheddingService service;

    public LoadSheddingController(LoadSheddingService service) {
        this.service = service;
    }

    @PostMapping("/trigger/{forecastId}")
    public void trigger(@PathVariable Long forecastId) {
        service.triggerLoadShedding(forecastId);
    }
}
