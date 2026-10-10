package com.nutrition.nutrition.service;

import com.nutrition.nutrition.dto.BeneficiaryRequest;
import com.nutrition.nutrition.entity.Beneficiary;
import com.nutrition.nutrition.repository.BeneficiaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository repository;

    public BeneficiaryService(BeneficiaryRepository repository) {
        this.repository = repository;
    }

    public List<Beneficiary> getAll() {
        return repository.findAll();
    }

    public Optional<Beneficiary> getById(Long id) {
        return repository.findById(id);
    }

    public Beneficiary add(BeneficiaryRequest request) {
        Beneficiary beneficiary = new Beneficiary();
        applyRequest(beneficiary, request);
        return repository.save(beneficiary);
    }

    public Optional<Beneficiary> update(
            Long id, BeneficiaryRequest request) {
        return repository.findById(id).map(beneficiary -> {
            applyRequest(beneficiary, request);
            return repository.save(beneficiary);
        });
    }

    private void applyRequest(
            Beneficiary beneficiary, BeneficiaryRequest request) {
        beneficiary.setName(request.getName());
        beneficiary.setAge(request.getAge());
        beneficiary.setHouseholdSize(
                request.getHouseholdSize() == null
                        ? 1
                        : request.getHouseholdSize());
        beneficiary.setNutritionNeeds(request.getNutritionNeeds());
        beneficiary.setAddress(request.getAddress());
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}