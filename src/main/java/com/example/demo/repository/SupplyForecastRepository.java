package com.example.demo.repository;

import java.util.Optional;
import com.example.demo.entity.SupplyForecast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyForecastRepository
        extends JpaRepository<SupplyForecast, Long> {

  Optional<SupplyForecast> findFirstByOrderByGeneratedAtDesc();

}
