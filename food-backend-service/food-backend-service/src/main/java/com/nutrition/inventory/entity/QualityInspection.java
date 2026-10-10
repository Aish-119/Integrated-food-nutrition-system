package com.nutrition.inventory.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "quality_inspections")
public class QualityInspection {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "food_item_id", nullable = false)
private FoodItem foodItem;

@ManyToOne
@JoinColumn(name = "batch_id")
private InventoryBatch batch;

private LocalDate inspectionDate = LocalDate.now();

@Column(length = 150)
private String inspectorName;

@Column(length = 50)
private String qualityStatus;

@Column(columnDefinition = "TEXT")
private String remarks;

public QualityInspection() {
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

public LocalDate getInspectionDate() {
    return inspectionDate;
}

public void setInspectionDate(LocalDate inspectionDate) {
    this.inspectionDate = inspectionDate;
}

public String getInspectorName() {
    return inspectorName;
}

public void setInspectorName(String inspectorName) {
    this.inspectorName = inspectorName;
}

public String getQualityStatus() {
    return qualityStatus;
}

public void setQualityStatus(String qualityStatus) {
    this.qualityStatus = qualityStatus;
}

public String getRemarks() {
    return remarks;
}

public void setRemarks(String remarks) {
    this.remarks = remarks;
}

}
