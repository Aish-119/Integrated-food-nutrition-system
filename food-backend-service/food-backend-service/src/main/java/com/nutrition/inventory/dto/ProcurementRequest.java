package com.nutrition.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProcurementRequest {

    private Long foodItemId;
    private BigDecimal quantity;
    private LocalDate procurementDate;
    private String supplier;

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
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
