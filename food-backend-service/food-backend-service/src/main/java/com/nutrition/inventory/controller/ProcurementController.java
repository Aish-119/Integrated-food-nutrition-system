package com.nutrition.inventory.controller;

import com.nutrition.inventory.dto.ProcurementRequest;
import com.nutrition.inventory.entity.Procurement;
import com.nutrition.inventory.service.ProcurementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procurements")
public class ProcurementController {

    private final ProcurementService service;

    public ProcurementController(ProcurementService service) {
        this.service = service;
    }

    @GetMapping
    public List<Procurement> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Procurement> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Procurement add(@RequestBody ProcurementRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Procurement> update(
            @PathVariable Long id,
            @RequestBody ProcurementRequest request) {
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