package com.nutrition.inventory.controller;

import com.nutrition.inventory.dto.StockMovementRequest;
import com.nutrition.inventory.entity.StockMovement;
import com.nutrition.inventory.service.StockMovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementService service;

    public StockMovementController(StockMovementService service) {
        this.service = service;
    }

    @GetMapping
    public List<StockMovement> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovement> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public StockMovement add(@RequestBody StockMovementRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockMovement> update(
            @PathVariable Long id,
            @RequestBody StockMovementRequest request) {
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