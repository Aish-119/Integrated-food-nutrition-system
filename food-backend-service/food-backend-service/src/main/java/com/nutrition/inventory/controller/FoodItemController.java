
package com.nutrition.inventory.controller;

import com.nutrition.inventory.entity.FoodItem;
import com.nutrition.inventory.service.FoodItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/food-items")
public class FoodItemController {

    private final FoodItemService service;

    public FoodItemController(FoodItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<FoodItem> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public FoodItem add(@RequestBody FoodItem item) {
        return service.add(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> update(
            @PathVariable Long id,
            @RequestBody FoodItem item) {
        return service.update(id, item)
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
