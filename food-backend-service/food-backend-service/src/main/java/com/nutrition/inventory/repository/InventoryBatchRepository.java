package com.nutrition.inventory.repository;

import com.nutrition.inventory.entity.InventoryBatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryBatchRepository extends JpaRepository<InventoryBatch, Long> {
}
