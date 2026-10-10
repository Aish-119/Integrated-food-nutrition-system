package com.nutrition.inventory.repository;

import com.nutrition.inventory.entity.Procurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcurementRepository extends JpaRepository<Procurement, Long> {
}
