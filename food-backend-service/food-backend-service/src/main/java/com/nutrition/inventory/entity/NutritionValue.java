package com.nutrition.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "nutrition_values")
public class NutritionValue {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "food_item_id", nullable = false)
private FoodItem foodItem;

private BigDecimal calories;
private BigDecimal protein;
private BigDecimal carbohydrates;
private BigDecimal fat;

@Column(columnDefinition = "TEXT")
private String vitamins;

@Column(columnDefinition = "TEXT")
private String minerals;

public NutritionValue() {
}

public Long getId() {
    return id;
}

public FoodItem getFoodItem() {
    return foodItem;
}

public void setFoodItem(FoodItem foodItem) {
    this.foodItem = foodItem;
}

public BigDecimal getCalories() {
    return calories;
}

public void setCalories(BigDecimal calories) {
    this.calories = calories;
}

public BigDecimal getProtein() {
    return protein;
}

public void setProtein(BigDecimal protein) {
    this.protein = protein;
}

public BigDecimal getCarbohydrates() {
    return carbohydrates;
}

public void setCarbohydrates(BigDecimal carbohydrates) {
    this.carbohydrates = carbohydrates;
}

public BigDecimal getFat() {
    return fat;
}

public void setFat(BigDecimal fat) {
    this.fat = fat;
}

public String getVitamins() {
    return vitamins;
}

public void setVitamins(String vitamins) {
    this.vitamins = vitamins;
}

public String getMinerals() {
    return minerals;
}

public void setMinerals(String minerals) {
    this.minerals = minerals;
}

}
