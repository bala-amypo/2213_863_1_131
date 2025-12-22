package com.example.demo.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity

@Table(uniqueConstraints = @UniqueConstraint(columnNames = "zoneName"))
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zoneName;
    private Integer priorityLevel;
    private Integer population;
    private Boolean active = true;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }
    public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getZoneName() {
    return zoneName;
}

public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
}

public Integer getPriorityLevel() {
    return priorityLevel;
}

public void setPriorityLevel(Integer priorityLevel) {
    this.priorityLevel = priorityLevel;
}

public Integer getPopulation() {
    return population;
}

public void setPopulation(Integer population) {
    this.population = population;
}

public Boolean getActive() {
    return active;
}

public void setActive(Boolean active) {
    this.active = active;
}

public Timestamp getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
}

public Timestamp getUpdatedAt() {
    return updatedAt;
}

public void setUpdatedAt(Timestamp updatedAt) {
    this.updatedAt = updatedAt;
}

}
