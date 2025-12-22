package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "supply_forecasts")
public class SupplyForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double availableSupplyMW;

    private Instant forecastStart;

    private Instant forecastEnd;

    private Instant generatedAt;

    public SupplyForecast() {
    }

    public SupplyForecast(Long id, Double availableSupplyMW, Instant forecastStart, Instant forecastEnd, Instant generatedAt) {
        this.id = id;
        this.availableSupplyMW = availableSupplyMW;
        this.forecastStart = forecastStart;
        this.forecastEnd = forecastEnd;
        this.generatedAt = generatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAvailableSupplyMW() {
        return availableSupplyMW;
    }

    public void setAvailableSupplyMW(Double availableSupplyMW) {
        this.availableSupplyMW = availableSupplyMW;
    }

    public Instant getForecastStart() {
        return forecastStart;
    }

    public void setForecastStart(Instant forecastStart) {
        this.forecastStart = forecastStart;
    }

    public Instant getForecastEnd() {
        return forecastEnd;
    }

    public void setForecastEnd(Instant forecastEnd) {
        this.forecastEnd = forecastEnd;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    @PrePersist
    protected void onCreate() {
        generatedAt = Instant.now();
    }
}
