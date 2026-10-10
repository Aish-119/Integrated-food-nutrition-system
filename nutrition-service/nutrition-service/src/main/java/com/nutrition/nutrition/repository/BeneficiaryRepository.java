package com.nutrition.nutrition.repository;

import com.nutrition.nutrition.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
}

