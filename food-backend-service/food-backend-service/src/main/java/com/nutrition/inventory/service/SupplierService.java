
package com.nutrition.inventory.service;

import com.nutrition.inventory.entity.Supplier;
import com.nutrition.inventory.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository repository;

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    public List<Supplier> getAll() {
        return repository.findAll();
    }

    public Optional<Supplier> getById(Long id) {
        return repository.findById(id);
    }

    public Supplier add(Supplier supplier) {
        return repository.save(supplier);
    }

    public Optional<Supplier> update(Long id, Supplier updated) {
        return repository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setContactPerson(updated.getContactPerson());
            existing.setPhone(updated.getPhone());
            existing.setEmail(updated.getEmail());
            existing.setAddress(updated.getAddress());
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
