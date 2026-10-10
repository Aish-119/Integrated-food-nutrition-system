
package com.nutrition.inventory.service;

import com.nutrition.inventory.entity.FoodItem;
import com.nutrition.inventory.repository.FoodItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    private final FoodItemRepository repository;

    public FoodItemService(FoodItemRepository repository) {
        this.repository = repository;
    }

    public List<FoodItem> getAll() {
        return repository.findAll();
    }

    public Optional<FoodItem> getById(Long id) {
        return repository.findById(id);
    }

    public FoodItem add(FoodItem item) {
        return repository.save(item);
    }

    public Optional<FoodItem> update(Long id, FoodItem updatedItem) {
        return repository.findById(id).map(existing -> {
            existing.setName(updatedItem.getName());
            existing.setCategory(updatedItem.getCategory());
            existing.setQuantity(updatedItem.getQuantity());
            existing.setUnit(updatedItem.getUnit());
            existing.setExpiryDate(updatedItem.getExpiryDate());
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
