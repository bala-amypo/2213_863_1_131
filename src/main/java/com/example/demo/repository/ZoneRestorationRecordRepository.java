package com.example.demo.repository;

import com.example.demo.entity.ZoneRestorationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public interface ZoneRestorationRecordRepository extends JpaRepository<ZoneRestorationRecord, Long> {
    List<ZoneRestorationRecord> findByZoneId(Long zoneId);
}
