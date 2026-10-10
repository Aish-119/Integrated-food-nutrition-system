package com.nutrition.inventory.controller;

import com.nutrition.inventory.dto.InventoryBatchRequest;
import com.nutrition.inventory.entity.InventoryBatch;
import com.nutrition.inventory.service.InventoryBatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-batches")
public class InventoryBatchController {

    private final InventoryBatchService service;

    public InventoryBatchController(InventoryBatchService service) {
        this.service = service;
    }

    @GetMapping
    public List<InventoryBatch> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryBatch> getById(
            @PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public InventoryBatch add(
            @RequestBody InventoryBatchRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryBatch> update(
            @PathVariable Long id,
            @RequestBody InventoryBatchRequest request) {
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