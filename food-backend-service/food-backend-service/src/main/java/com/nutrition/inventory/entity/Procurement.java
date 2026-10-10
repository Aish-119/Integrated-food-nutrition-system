package com.nutrition.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "procurements")
public class Procurement {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "food_item_id", nullable = false)
private FoodItem foodItem;

@Column(nullable = false)
private BigDecimal quantity;

@Column(name = "procurement_date", nullable = false)
private LocalDate procurementDate = LocalDate.now();

private String supplier;

public Procurement() {
}

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
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

public LocalDate getProcurementDate() {
    return procurementDate;
}

public void setProcurementDate(LocalDate procurementDate) {
    this.procurementDate = procurementDate;
}

public String getSupplier() {
    return supplier;
}

public void setSupplier(String supplier) {
    this.supplier = supplier;
}

}
