package com.example.demo.service;

import com.example.demo.entity.Zone;
import java.util.List;

public interface ZoneService {
    Zone create(Zone zone);
    Zone update(Long id, Zone zone);
    Zone getById(Long id);
    List<Zone> getAll();
    void deactivate(Long id);
}
