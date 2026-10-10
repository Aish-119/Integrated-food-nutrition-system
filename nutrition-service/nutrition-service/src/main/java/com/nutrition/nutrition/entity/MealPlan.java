package com.nutrition.nutrition.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "meal_plans")
public class MealPlan {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "programme_id")
private NutritionProgramme programme;

@Column(nullable = false, length = 150)
private String name;

@Column(length = 50)
private String mealType;

@Column(columnDefinition = "TEXT")
private String description;

private LocalDate plannedDate;

@Column(columnDefinition = "TEXT")
private String notes;

public MealPlan() {
}

public Long getId() {
    return id;
}

public NutritionProgramme getProgramme() {
    return programme;
}

public void setProgramme(NutritionProgramme programme) {
    this.programme = programme;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getMealType() {
    return mealType;
}

public void setMealType(String mealType) {
    this.mealType = mealType;
}

public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public LocalDate getPlannedDate() {
    return plannedDate;
}

public void setPlannedDate(LocalDate plannedDate) {
    this.plannedDate = plannedDate;
}

public String getNotes() {
    return notes;
}

public void setNotes(String notes) {
    this.notes = notes;
}

}

