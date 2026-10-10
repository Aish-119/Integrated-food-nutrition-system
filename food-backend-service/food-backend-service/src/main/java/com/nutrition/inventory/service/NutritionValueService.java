package com.nutrition.inventory.service;

import com.nutrition.inventory.dto.NutritionValueRequest;
import com.nutrition.inventory.entity.NutritionValue;
import com.nutrition.inventory.repository.NutritionValueRepository;
import com.nutrition.inventory.repository.FoodItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class NutritionValueService {

    private final NutritionValueRepository repository;
    private final FoodItemRepository foodItemRepository;

    public NutritionValueService(
            NutritionValueRepository repository,
            FoodItemRepository foodItemRepository) {
        this.repository = repository;
        this.foodItemRepository = foodItemRepository;
    }

    public List<NutritionValue> getAll() {
        return repository.findAll();
    }

    public Optional<NutritionValue> getById(Long id) {
        return repository.findById(id);
    }

    public NutritionValue add(NutritionValueRequest request) {
        NutritionValue value = new NutritionValue();
        applyRequest(value, request);
        return repository.save(value);
    }

    public Optional<NutritionValue> update(
            Long id, NutritionValueRequest request) {
        return repository.findById(id).map(value -> {
            applyRequest(value, request);
            return repository.save(value);
        });
    }

    private void applyRequest(
            NutritionValue value, NutritionValueRequest request) {

        var foodItem = foodItemRepository.findById(
                request.getFoodItemId()).orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Food item not found"));

        value.setFoodItem(foodItem);
        value.setCalories(request.getCalories());
        value.setProtein(request.getProtein());
        value.setCarbohydrates(request.getCarbohydrates());
        value.setFat(request.getFat());
        value.setVitamins(request.getVitamins());
        value.setMinerals(request.getMinerals());
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}