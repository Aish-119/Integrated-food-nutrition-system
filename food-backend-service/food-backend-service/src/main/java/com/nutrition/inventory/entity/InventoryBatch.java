package com.nutrition.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "inventory_batches")
public class InventoryBatch {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "food_item_id", nullable = false)
private FoodItem foodItem;

@Column(length = 100)
private String batchNumber;

@Column(nullable = false, precision = 12, scale = 2)
private BigDecimal quantity;

@Column(nullable = false, length = 30)
private String unit;

private LocalDate receivedDate = LocalDate.now();

private LocalDate expiryDate;

public InventoryBatch() {
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

public String getBatchNumber() {
    return batchNumber;
}

public void setBatchNumber(String batchNumber) {
    this.batchNumber = batchNumber;
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

public LocalDate getReceivedDate() {
    return receivedDate;
}

public void setReceivedDate(LocalDate receivedDate) {
    this.receivedDate = receivedDate;
}

public LocalDate getExpiryDate() {
    return expiryDate;
}

public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
}

}
