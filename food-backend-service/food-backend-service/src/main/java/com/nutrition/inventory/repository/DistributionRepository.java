package com.nutrition.inventory.repository;

import com.nutrition.inventory.entity.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributionRepository extends JpaRepository<Distribution, Long> {
}
