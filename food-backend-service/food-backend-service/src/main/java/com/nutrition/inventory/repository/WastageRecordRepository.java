package com.nutrition.inventory.repository;

import com.nutrition.inventory.entity.WastageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WastageRecordRepository extends JpaRepository<WastageRecord, Long> {
}
