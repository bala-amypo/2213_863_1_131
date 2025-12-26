
package com.example.demo.service;

import com.example.demo.entity.Zone;
import java.util.List;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public interface ZoneService {
    void save(Zone zone);
    List<Zone> getAllZones();
}
