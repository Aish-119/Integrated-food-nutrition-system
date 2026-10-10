package com.nutrition.inventory.repository;

import com.nutrition.inventory.entity.DistributionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributionItemRepository extends JpaRepository<DistributionItem, Long> {
}
