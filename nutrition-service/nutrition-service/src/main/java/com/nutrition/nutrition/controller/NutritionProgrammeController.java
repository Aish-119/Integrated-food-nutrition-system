package com.nutrition.nutrition.controller;

import com.nutrition.nutrition.entity.NutritionProgramme;
import com.nutrition.nutrition.repository.NutritionProgrammeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/programmes")
public class NutritionProgrammeController {

    private final NutritionProgrammeRepository repository;

    public NutritionProgrammeController(
            NutritionProgrammeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<NutritionProgramme> getAllProgrammes() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public NutritionProgramme getProgramme(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Nutrition programme not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NutritionProgramme createProgramme(
            @RequestBody NutritionProgramme programme) {

        if (programme.getName() == null
                || programme.getName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Programme name is required");
        }

        if (programme.getStartDate() != null
                && programme.getEndDate() != null
                && programme.getEndDate().isBefore(programme.getStartDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date cannot be before start date");
        }

        programme.setName(programme.getName().trim());

        if (programme.getStatus() == null
                || programme.getStatus().isBlank()) {
            programme.setStatus("ACTIVE");
        }

        return repository.save(programme);
    }
}