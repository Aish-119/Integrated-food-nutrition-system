package com.nutrition.inventory.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "distributions")
public class Distribution {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "beneficiary_id", nullable = false)
private Beneficiary beneficiary;

@Column(name = "distribution_date", nullable = false)
private LocalDate distributionDate = LocalDate.now();

@Column(nullable = false)
private String status = "PLANNED";

public Distribution() {
}

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public Beneficiary getBeneficiary() {
    return beneficiary;
}

public void setBeneficiary(Beneficiary beneficiary) {
    this.beneficiary = beneficiary;
}

public LocalDate getDistributionDate() {
    return distributionDate;
}

public void setDistributionDate(LocalDate distributionDate) {
    this.distributionDate = distributionDate;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

}
