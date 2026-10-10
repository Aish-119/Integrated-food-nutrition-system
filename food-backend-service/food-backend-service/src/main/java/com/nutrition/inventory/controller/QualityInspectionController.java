package com.nutrition.inventory.controller;

import com.nutrition.inventory.dto.QualityInspectionRequest;
import com.nutrition.inventory.entity.QualityInspection;
import com.nutrition.inventory.service.QualityInspectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quality-inspections")
public class QualityInspectionController {

    private final QualityInspectionService service;

    public QualityInspectionController(QualityInspectionService service) {
        this.service = service;
    }

    @GetMapping
    public List<QualityInspection> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QualityInspection> getById(
            @PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public QualityInspection add(
            @RequestBody QualityInspectionRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QualityInspection> update(
            @PathVariable Long id,
            @RequestBody QualityInspectionRequest request) {
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