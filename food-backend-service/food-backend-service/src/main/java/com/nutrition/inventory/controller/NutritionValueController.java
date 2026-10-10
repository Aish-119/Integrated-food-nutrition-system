package com.nutrition.inventory.controller;

import com.nutrition.inventory.dto.NutritionValueRequest;
import com.nutrition.inventory.entity.NutritionValue;
import com.nutrition.inventory.service.NutritionValueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutrition-values")
public class NutritionValueController {

    private final NutritionValueService service;

    public NutritionValueController(NutritionValueService service) {
        this.service = service;
    }

    @GetMapping
    public List<NutritionValue> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NutritionValue> getById(
            @PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public NutritionValue add(
            @RequestBody NutritionValueRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NutritionValue> update(
            @PathVariable Long id,
            @RequestBody NutritionValueRequest request) {
        return service.update(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}