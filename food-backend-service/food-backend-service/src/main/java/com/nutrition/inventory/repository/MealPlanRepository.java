package com.nutrition.inventory.repository;

import com.nutrition.inventory.entity.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
}
