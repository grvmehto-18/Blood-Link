package com.blood.bank.Blood.bank.Repository;

import com.blood.bank.Blood.bank.model.BloodCamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodCampRepository extends JpaRepository<BloodCamp, Long> {
}