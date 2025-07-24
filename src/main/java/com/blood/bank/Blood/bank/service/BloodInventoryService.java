package com.blood.bank.Blood.bank.service;

import com.blood.bank.Blood.bank.Repository.BloodInventoryRepository;
import com.blood.bank.Blood.bank.model.BloodInventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BloodInventoryService {

    private final BloodInventoryRepository bloodInventoryRepository;

    public List<BloodInventory> getAllInventory() {
        return bloodInventoryRepository.findAll();
    }

    public Optional<BloodInventory> getInventoryById(Long id) {
        return bloodInventoryRepository.findById(id);
    }

    public BloodInventory addOrUpdateInventory(BloodInventory bloodInventory) {
        // Check if an entry for this blood group and location already exists
        Optional<BloodInventory> existingInventory = bloodInventoryRepository.findByBloodGroupAndLocation(
                bloodInventory.getBloodGroup(), bloodInventory.getLocation());

        if (existingInventory.isPresent()) {
            BloodInventory inventoryToUpdate = existingInventory.get();
            inventoryToUpdate.setQuantityMl(inventoryToUpdate.getQuantityMl() + bloodInventory.getQuantityMl());
            return bloodInventoryRepository.save(inventoryToUpdate);
        } else {
            return bloodInventoryRepository.save(bloodInventory);
        }
    }

    public void deleteInventory(Long id) {
        bloodInventoryRepository.deleteById(id);
    }

    public Optional<BloodInventory> getInventoryByBloodGroupAndLocation(String bloodGroup, String location) {
        return bloodInventoryRepository.findByBloodGroupAndLocation(bloodGroup, location);
    }

    public boolean deductBlood(String bloodGroup, String location, int quantity) {
        Optional<BloodInventory> inventoryOptional = bloodInventoryRepository.findByBloodGroupAndLocation(bloodGroup, location);
        if (inventoryOptional.isPresent()) {
            BloodInventory inventory = inventoryOptional.get();
            if (inventory.getQuantityMl() >= quantity) {
                inventory.setQuantityMl(inventory.getQuantityMl() - quantity);
                bloodInventoryRepository.save(inventory);
                return true;
            }
        }
        return false;
    }
}