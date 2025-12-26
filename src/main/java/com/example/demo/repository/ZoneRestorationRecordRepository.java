package com.example.demo.repository;

import com.example.demo.entity.ZoneRestorationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public interface ZoneRestorationRecordRepository extends JpaRepository<ZoneRestorationRecord, Long> {
    List<ZoneRestorationRecord> findByZoneId(Long zoneId);
}
