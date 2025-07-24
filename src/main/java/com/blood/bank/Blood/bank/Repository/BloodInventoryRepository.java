package com.blood.bank.Blood.bank.Repository;

import com.blood.bank.Blood.bank.model.BloodInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {
    Optional<BloodInventory> findByBloodGroupAndLocation(String bloodGroup, String location);
}