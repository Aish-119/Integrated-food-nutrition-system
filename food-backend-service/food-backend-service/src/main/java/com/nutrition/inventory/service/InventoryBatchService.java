package com.nutrition.inventory.service;

import com.nutrition.inventory.dto.InventoryBatchRequest;
import com.nutrition.inventory.entity.InventoryBatch;
import com.nutrition.inventory.repository.InventoryBatchRepository;
import com.nutrition.inventory.repository.FoodItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryBatchService {

    private final InventoryBatchRepository repository;
    private final FoodItemRepository foodItemRepository;

    public InventoryBatchService(
            InventoryBatchRepository repository,
            FoodItemRepository foodItemRepository) {
        this.repository = repository;
        this.foodItemRepository = foodItemRepository;
    }

    public List<InventoryBatch> getAll() {
        return repository.findAll();
    }

    public Optional<InventoryBatch> getById(Long id) {
        return repository.findById(id);
    }

    public InventoryBatch add(InventoryBatchRequest request) {
        InventoryBatch batch = new InventoryBatch();
        applyRequest(batch, request);
        return repository.save(batch);
    }

    public Optional<InventoryBatch> update(
            Long id, InventoryBatchRequest request) {
        return repository.findById(id).map(batch -> {
            applyRequest(batch, request);
            return repository.save(batch);
        });
    }

    private void applyRequest(
            InventoryBatch batch, InventoryBatchRequest request) {

        var foodItem = foodItemRepository.findById(
                request.getFoodItemId()).orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Food item not found"));

        batch.setFoodItem(foodItem);
        batch.setBatchNumber(request.getBatchNumber());
        batch.setQuantity(request.getQuantity());
        batch.setUnit(request.getUnit());

        if (request.getReceivedDate() != null) {
            batch.setReceivedDate(request.getReceivedDate());
        }

        batch.setExpiryDate(request.getExpiryDate());
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}