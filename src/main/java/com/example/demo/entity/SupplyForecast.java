package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplyForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double availableSupplyMW;
    private Timestamp forecastStart;
    private Timestamp forecastEnd;
    private Timestamp generatedAt;

    @PrePersist
    public void generateTime() {
        generatedAt = new Timestamp(System.currentTimeMillis());
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

public Timestamp getForecastStart() {
    return forecastStart;
}

public void setForecastStart(Timestamp forecastStart) {
    this.forecastStart = forecastStart;
}

public Timestamp getForecastEnd() {
    return forecastEnd;
}

public void setForecastEnd(Timestamp forecastEnd) {
    this.forecastEnd = forecastEnd;
}

public Timestamp getGeneratedAt() {
    return generatedAt;
}

public void setGeneratedAt(Timestamp generatedAt) {
    this.generatedAt = generatedAt;
}

}
