package com.nutrition.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "distribution_items")
public class DistributionItem {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "distribution_id", nullable = false)
private Distribution distribution;

@ManyToOne
@JoinColumn(name = "food_item_id", nullable = false)
private FoodItem foodItem;

@Column(nullable = false)
private BigDecimal quantity;

@Column(nullable = false)
private String unit;

public DistributionItem() {
}

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public Distribution getDistribution() {
    return distribution;
}

public void setDistribution(Distribution distribution) {
    this.distribution = distribution;
}

public FoodItem getFoodItem() {
    return foodItem;
}

public void setFoodItem(FoodItem foodItem) {
    this.foodItem = foodItem;
}

public BigDecimal getQuantity() {
    return quantity;
}

public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
}

public String getUnit() {
    return unit;
}

public void setUnit(String unit) {
    this.unit = unit;
}

}
