package com.nutrition.inventory.service;

import com.nutrition.inventory.dto.ProcurementRequest;
import com.nutrition.inventory.entity.Procurement;
import com.nutrition.inventory.repository.ProcurementRepository;
import com.nutrition.inventory.repository.FoodItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProcurementService {

    private final ProcurementRepository repository;
    private final FoodItemRepository foodItemRepository;

    public ProcurementService(
            ProcurementRepository repository,
            FoodItemRepository foodItemRepository) {
        this.repository = repository;
        this.foodItemRepository = foodItemRepository;
    }

    public List<Procurement> getAll() {
        return repository.findAll();
    }

    public Optional<Procurement> getById(Long id) {
        return repository.findById(id);
    }

    public Procurement add(ProcurementRequest request) {
        Procurement procurement = new Procurement();
        applyRequest(procurement, request);
        return repository.save(procurement);
    }

    public Optional<Procurement> update(
            Long id, ProcurementRequest request) {
        return repository.findById(id).map(procurement -> {
            applyRequest(procurement, request);
            return repository.save(procurement);
        });
    }

    private void applyRequest(
            Procurement procurement, ProcurementRequest request) {

        var foodItem = foodItemRepository.findById(
                request.getFoodItemId()).orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Food item not found"));

        procurement.setFoodItem(foodItem);
        procurement.setQuantity(request.getQuantity());
        procurement.setSupplier(request.getSupplier());

        if (request.getProcurementDate() != null) {
            procurement.setProcurementDate(request.getProcurementDate());
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