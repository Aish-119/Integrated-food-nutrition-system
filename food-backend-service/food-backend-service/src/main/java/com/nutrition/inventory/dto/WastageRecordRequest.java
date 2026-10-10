package com.nutrition.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class WastageRecordRequest {

    private Long foodItemId;
    private Long batchId;
    private BigDecimal quantity;
    private String unit;
    private String wastageReason;
    private LocalDate wastageDate;
    private String remarks;

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
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