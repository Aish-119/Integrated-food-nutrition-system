package com.nutrition.inventory.repository;

import com.nutrition.inventory.entity.NutritionValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutritionValueRepository extends JpaRepository<NutritionValue, Long> {
}
