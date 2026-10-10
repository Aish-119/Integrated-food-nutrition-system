package com.nutrition.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "wastage_records")
public class WastageRecord {
 
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "food_item_id", nullable = false)
private FoodItem foodItem;

@ManyToOne
@JoinColumn(name = "batch_id")
private InventoryBatch batch;

@Column(nullable = false, precision = 12, scale = 2)
private BigDecimal quantity;

@Column(nullable = false, length = 30)
private String unit;

@Column(length = 150)
private String wastageReason;

private LocalDate wastageDate = LocalDate.now();

@Column(columnDefinition = "TEXT")
private String remarks;

public WastageRecord() {
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

public InventoryBatch getBatch() {
    return batch;
}

public void setBatch(InventoryBatch batch) {
    this.batch = batch;
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

public String getWastageReason() {
    return wastageReason;
}

public void setWastageReason(String wastageReason) {
    this.wastageReason = wastageReason;
}

public LocalDate getWastageDate() {
    return wastageDate;
}

public void setWastageDate(LocalDate wastageDate) {
    this.wastageDate = wastageDate;
}

public String getRemarks() {
    return remarks;
}

public void setRemarks(String remarks) {
    this.remarks = remarks;
}

}
