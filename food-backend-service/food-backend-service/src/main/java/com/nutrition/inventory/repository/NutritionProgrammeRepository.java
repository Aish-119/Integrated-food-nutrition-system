package com.nutrition.inventory.repository;

import com.nutrition.inventory.entity.NutritionProgramme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutritionProgrammeRepository extends JpaRepository<NutritionProgramme, Long> {
}
