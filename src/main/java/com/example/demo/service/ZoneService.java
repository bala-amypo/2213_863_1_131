
package com.example.demo.service;

import com.example.demo.entity.Zone;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public interface ZoneService {
    void save(Zone zone);
    List<Zone> getAllZones();
}
