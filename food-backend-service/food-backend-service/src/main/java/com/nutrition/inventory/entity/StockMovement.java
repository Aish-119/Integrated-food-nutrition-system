package com.nutrition.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock_movements")
public class StockMovement {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "food_item_id", nullable = false)
private FoodItem foodItem;

@ManyToOne
@JoinColumn(name = "batch_id")
private InventoryBatch batch;

@Column(nullable = false, length = 30)
private String movementType;

@Column(nullable = false, precision = 12, scale = 2)
private BigDecimal quantity;

private LocalDate movementDate = LocalDate.now();

@Column(columnDefinition = "TEXT")
private String remarks;

public StockMovement() {
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

public String getMovementType() {
    return movementType;
}

public void setMovementType(String movementType) {
    this.movementType = movementType;
}

public BigDecimal getQuantity() {
    return quantity;
}

public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
}

public LocalDate getMovementDate() {
    return movementDate;
}

public void setMovementDate(LocalDate movementDate) {
    this.movementDate = movementDate;
}

public String getRemarks() {
    return remarks;
}

public void setRemarks(String remarks) {
    this.remarks = remarks;
}

}
