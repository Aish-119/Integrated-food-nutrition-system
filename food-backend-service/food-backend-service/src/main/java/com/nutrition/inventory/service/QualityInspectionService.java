
package com.nutrition.inventory.service;

import com.nutrition.inventory.dto.QualityInspectionRequest;
import com.nutrition.inventory.entity.QualityInspection;
import com.nutrition.inventory.repository.QualityInspectionRepository;
import com.nutrition.inventory.repository.FoodItemRepository;
import com.nutrition.inventory.repository.InventoryBatchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class QualityInspectionService {

    private final QualityInspectionRepository repository;
    private final FoodItemRepository foodItemRepository;
    private final InventoryBatchRepository batchRepository;

    public QualityInspectionService(
            QualityInspectionRepository repository,
            FoodItemRepository foodItemRepository,
            InventoryBatchRepository batchRepository) {
        this.repository = repository;
        this.foodItemRepository = foodItemRepository;
        this.batchRepository = batchRepository;
    }

    public List<QualityInspection> getAll() {
        return repository.findAll();
    }

    public Optional<QualityInspection> getById(Long id) {
        return repository.findById(id);
    }

    public QualityInspection add(QualityInspectionRequest request) {
        QualityInspection inspection = new QualityInspection();
        applyRequest(inspection, request);
        return repository.save(inspection);
    }

    public Optional<QualityInspection> update(
            Long id, QualityInspectionRequest request) {
        return repository.findById(id).map(inspection -> {
            applyRequest(inspection, request);
            return repository.save(inspection);
        });
    }

    private void applyRequest(
            QualityInspection inspection,
            QualityInspectionRequest request) {

        var foodItem = foodItemRepository.findById(
                request.getFoodItemId()).orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Food item not found"));

        inspection.setFoodItem(foodItem);

        if (request.getBatchId() == null) {
            inspection.setBatch(null);
        } else {
            var batch = batchRepository.findById(
                    request.getBatchId()).orElseThrow(
                            () -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Inventory batch not found"));

            if (!batch.getFoodItem().getId().equals(foodItem.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Batch does not belong to this food item");
            }

            inspection.setBatch(batch);
        }

        inspection.setInspectorName(request.getInspectorName());
        inspection.setQualityStatus(request.getQualityStatus());
        inspection.setRemarks(request.getRemarks());

        if (request.getInspectionDate() != null) {
            inspection.setInspectionDate(request.getInspectionDate());
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