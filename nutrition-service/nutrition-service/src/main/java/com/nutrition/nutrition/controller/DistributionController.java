package com.nutrition.nutrition.controller;

import com.nutrition.nutrition.dto.*;
import com.nutrition.nutrition.entity.*;
import com.nutrition.nutrition.service.DistributionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/distributions")
public class DistributionController {
    private final DistributionService service;

    public DistributionController(DistributionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Distribution> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}/items")
    public List<DistributionItem> getItems(@PathVariable Long id) {
        return service.getItems(id);
    }

    @PostMapping("/check")
    public List<StockCheckResult> check(@RequestBody DistributionCreateRequest request) {
        return service.checkStock(request.items());
    }

    @PostMapping
    public Distribution create(@RequestBody DistributionCreateRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{id}/status")
    public Distribution updateStatus(@PathVariable Long id,
            @RequestBody DistributionStatusRequest request) {
        return service.updateStatus(id, request.status());
    }

    @PostMapping("/{id}/status")
    public Distribution updateStatusFromForm(
            @PathVariable Long id,
            @RequestBody DistributionStatusRequest request) {
        return service.updateStatus(id, request.status());
    }
}
