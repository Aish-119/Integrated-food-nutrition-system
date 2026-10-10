package com.nutrition.inventory.controller;

import com.nutrition.inventory.dto.WastageRecordRequest;
import com.nutrition.inventory.entity.WastageRecord;
import com.nutrition.inventory.service.WastageRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wastage-records")
public class WastageRecordController {

    private final WastageRecordService service;

    public WastageRecordController(WastageRecordService service) {
        this.service = service;
    }

    @GetMapping
    public List<WastageRecord> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WastageRecord> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public WastageRecord add(@RequestBody WastageRecordRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WastageRecord> update(
            @PathVariable Long id,
            @RequestBody WastageRecordRequest request) {
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