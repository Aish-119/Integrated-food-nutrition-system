package com.nutrition.inventory.service;

import com.nutrition.inventory.dto.WastageRecordRequest;
import com.nutrition.inventory.entity.WastageRecord;
import com.nutrition.inventory.repository.WastageRecordRepository;
import com.nutrition.inventory.repository.FoodItemRepository;
import com.nutrition.inventory.repository.InventoryBatchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class WastageRecordService {

    private final WastageRecordRepository repository;
    private final FoodItemRepository foodItemRepository;
    private final InventoryBatchRepository batchRepository;

    public WastageRecordService(
            WastageRecordRepository repository,
            FoodItemRepository foodItemRepository,
            InventoryBatchRepository batchRepository) {
        this.repository = repository;
        this.foodItemRepository = foodItemRepository;
        this.batchRepository = batchRepository;
    }

    public List<WastageRecord> getAll() {
        return repository.findAll();
    }

    public Optional<WastageRecord> getById(Long id) {
        return repository.findById(id);
    }

    public WastageRecord add(WastageRecordRequest request) {
        WastageRecord record = new WastageRecord();
        applyRequest(record, request);
        return repository.save(record);
    }

    public Optional<WastageRecord> update(
            Long id, WastageRecordRequest request) {
        return repository.findById(id).map(record -> {
            applyRequest(record, request);
            return repository.save(record);
        });
    }

    private void applyRequest(
            WastageRecord record, WastageRecordRequest request) {

        var foodItem = foodItemRepository.findById(
                request.getFoodItemId()).orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Food item not found"));

        record.setFoodItem(foodItem);

        if (request.getBatchId() == null) {
            record.setBatch(null);
        } else {
            var batch = batchRepository.findById(
                    request.getBatchId()).orElseThrow(
                            () -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Inventory batch not found"));

            if (!batch.getFoodItem().getId().equals(foodItem.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Batch does not belong to this food item");
            }

            record.setBatch(batch);
        }

        record.setQuantity(request.getQuantity());
        record.setUnit(request.getUnit());
        record.setWastageReason(request.getWastageReason());
        record.setRemarks(request.getRemarks());

        if (request.getWastageDate() != null) {
            record.setWastageDate(request.getWastageDate());
        }
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}