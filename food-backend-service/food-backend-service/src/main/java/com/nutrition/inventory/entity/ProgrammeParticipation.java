package com.nutrition.inventory.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "programme_participation")
public class ProgrammeParticipation {
    
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "programme_id", nullable = false)
private NutritionProgramme programme;

@ManyToOne
@JoinColumn(name = "beneficiary_id", nullable = false)
private Beneficiary beneficiary;

private LocalDate enrollmentDate = LocalDate.now();

@Column(length = 30)
private String status = "ACTIVE";

public ProgrammeParticipation() {
}

public Long getId() {
    return id;
}

public NutritionProgramme getProgramme() {
    return programme;
}

public void setProgramme(NutritionProgramme programme) {
    this.programme = programme;
}

public Beneficiary getBeneficiary() {
    return beneficiary;
}

public void setBeneficiary(Beneficiary beneficiary) {
    this.beneficiary = beneficiary;
}

public LocalDate getEnrollmentDate() {
    return enrollmentDate;
}

public void setEnrollmentDate(LocalDate enrollmentDate) {
    this.enrollmentDate = enrollmentDate;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

}
