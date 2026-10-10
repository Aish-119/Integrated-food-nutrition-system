package com.nutrition.inventory.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "nutrition_programmes")
public class NutritionProgramme {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false, length = 150)
private String name;

@Column(columnDefinition = "TEXT")
private String description;

private LocalDate startDate;

private LocalDate endDate;

@Column(length = 30)
private String status = "ACTIVE";

public NutritionProgramme() {
}

public Long getId() {
    return id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public LocalDate getStartDate() {
    return startDate;
}

public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
}

public LocalDate getEndDate() {
    return endDate;
}

public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

}
