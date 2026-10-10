package com.nutrition.inventory.repository;

import com.nutrition.inventory.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
