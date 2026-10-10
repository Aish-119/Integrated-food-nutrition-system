
package com.nutrition.inventory.repository;

import com.nutrition.inventory.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository
        extends JpaRepository<Beneficiary, Long> {
}