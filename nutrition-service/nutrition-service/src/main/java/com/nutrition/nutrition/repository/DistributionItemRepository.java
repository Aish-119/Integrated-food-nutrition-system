package com.nutrition.nutrition.repository;

import com.nutrition.nutrition.entity.DistributionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DistributionItemRepository
        extends JpaRepository<DistributionItem, Long> {

    List<DistributionItem> findByDistributionId(Long distributionId);
}