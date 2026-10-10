package com.nutrition.inventory.service;

import com.nutrition.inventory.dto.StockMovementRequest;
import com.nutrition.inventory.entity.StockMovement;
import com.nutrition.inventory.repository.StockMovementRepository;
import com.nutrition.inventory.repository.FoodItemRepository;
import com.nutrition.inventory.repository.InventoryBatchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class StockMovementService {

    private final StockMovementRepository repository;
    private final FoodItemRepository foodItemRepository;
    private final InventoryBatchRepository batchRepository;

    public StockMovementService(
            StockMovementRepository repository,
            FoodItemRepository foodItemRepository,
            InventoryBatchRepository batchRepository) {
        this.repository = repository;
        this.foodItemRepository = foodItemRepository;
        this.batchRepository = batchRepository;
    }

    public List<StockMovement> getAll() {
        return repository.findAll();
    }

    public Optional<StockMovement> getById(Long id) {
        return repository.findById(id);
    }

    public StockMovement add(StockMovementRequest request) {
        StockMovement movement = new StockMovement();
        applyRequest(movement, request);
        return repository.save(movement);
    }

    public Optional<StockMovement> update(
            Long id, StockMovementRequest request) {
        return repository.findById(id).map(movement -> {
            applyRequest(movement, request);
            return repository.save(movement);
        });
    }

    private void applyRequest(
            StockMovement movement, StockMovementRequest request) {

        var foodItem = foodItemRepository.findById(
                request.getFoodItemId()).orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Food item not found"));

        movement.setFoodItem(foodItem);

        if (request.getBatchId() == null) {
            movement.setBatch(null);
        } else {
            var batch = batchRepository.findById(
                    request.getBatchId()).orElseThrow(
                            () -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Inventory batch not found"));

            if (!batch.getFoodItem().getId().equals(foodItem.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Batch does not belong to the specified food item");
            }

            movement.setBatch(batch);
        }

        movement.setMovementType(request.getMovementType());
        movement.setQuantity(request.getQuantity());
        movement.setRemarks(request.getRemarks());

        if (request.getMovementDate() != null) {
            movement.setMovementDate(request.getMovementDate());
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