package com.nutrition.inventory.service;

import com.nutrition.inventory.dto.WastageRecordRequest;
import com.nutrition.inventory.entity.FoodItem;
import com.nutrition.inventory.entity.InventoryBatch;
import com.nutrition.inventory.entity.WastageRecord;
import com.nutrition.inventory.repository.WastageRecordRepository;
import com.nutrition.inventory.repository.FoodItemRepository;
import com.nutrition.inventory.repository.InventoryBatchRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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

    @Transactional
    public WastageRecord add(WastageRecordRequest request) {
        WastageRecord record = new WastageRecord();

        applyRequest(record, request);
        deductStock(record);

        return repository.save(record);
    }

    @Transactional
    public Optional<WastageRecord> update(
            Long id, WastageRecordRequest request) {

        Optional<WastageRecord> found = repository.findById(id);

        if (found.isEmpty()) {
            return Optional.empty();
        }

        WastageRecord record = found.get();

        // Restore the old quantity only if it was previously deducted.
        restoreStock(record);

        applyRequest(record, request);
        deductStock(record);

        return Optional.of(repository.save(record));
    }

    private void applyRequest(
            WastageRecord record,
            WastageRecordRequest request) {

        if (request == null || request.getFoodItemId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Food item is required");
        }

        if (request.getBatchId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Select an inventory batch so stock can be deducted");
        }

        FoodItem foodItem = foodItemRepository.findById(
                request.getFoodItemId()).orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Food item not found"));

        InventoryBatch batch = batchRepository.findById(
                request.getBatchId()).orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Inventory batch not found"));

        if (!batch.getFoodItem().getId().equals(foodItem.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Batch does not belong to this food item");
        }

        BigDecimal quantity = request.getQuantity();

        if (quantity == null || quantity.signum() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Wastage quantity must be greater than zero");
        }

        if (request.getUnit() == null
                || request.getUnit().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unit is required");
        }

        if (!batch.getUnit().equalsIgnoreCase(request.getUnit())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Wastage unit must match the inventory batch unit: "
                            + batch.getUnit());
        }

        record.setFoodItem(foodItem);
        record.setBatch(batch);
        record.setQuantity(quantity);
        record.setUnit(request.getUnit().trim().toUpperCase());
        record.setWastageReason(request.getWastageReason());
        record.setRemarks(request.getRemarks());

        if (request.getWastageDate() != null) {
            record.setWastageDate(request.getWastageDate());
        }
    }

    private void deductStock(WastageRecord record) {
        InventoryBatch batch = record.getBatch();

        BigDecimal available = batch.getQuantity() == null
                ? BigDecimal.ZERO
                : batch.getQuantity();

        if (available.compareTo(record.getQuantity()) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Insufficient stock in batch "
                            + batch.getBatchNumber());
        }

        batch.setQuantity(available.subtract(record.getQuantity()));
        batchRepository.save(batch);

        record.setStockDeducted(true);
    }

    private void restoreStock(WastageRecord record) {
        if (!Boolean.TRUE.equals(record.getStockDeducted())
                || record.getBatch() == null) {
            return;
        }

        InventoryBatch batch = batchRepository.findById(
                record.getBatch().getId()).orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Original inventory batch not found"));

        BigDecimal current = batch.getQuantity() == null
                ? BigDecimal.ZERO
                : batch.getQuantity();

        batch.setQuantity(current.add(record.getQuantity()));
        batchRepository.save(batch);

        record.setStockDeducted(false);
    }

    @Transactional
    public boolean delete(Long id) {
        Optional<WastageRecord> found = repository.findById(id);

        if (found.isEmpty()) {
            return false;
        }

        WastageRecord record = found.get();
        restoreStock(record);
        repository.delete(record);

        return true;
    }
}