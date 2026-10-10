package com.nutrition.nutrition.controller;

import com.nutrition.nutrition.dto.BeneficiaryRequest;
import com.nutrition.nutrition.entity.Beneficiary;
import com.nutrition.nutrition.service.BeneficiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService service;

    public BeneficiaryController(BeneficiaryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Beneficiary> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beneficiary> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Beneficiary add(@RequestBody BeneficiaryRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Beneficiary> update(
            @PathVariable Long id,
            @RequestBody BeneficiaryRequest request) {
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