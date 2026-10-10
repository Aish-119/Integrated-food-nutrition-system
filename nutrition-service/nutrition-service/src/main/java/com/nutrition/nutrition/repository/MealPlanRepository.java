package com.nutrition.nutrition.repository;

import com.nutrition.nutrition.entity.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
}

