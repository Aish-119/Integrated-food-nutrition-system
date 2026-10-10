package com.nutrition.inventory.repository;

import com.nutrition.inventory.entity.QualityInspection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualityInspectionRepository extends JpaRepository<QualityInspection, Long> {
}
