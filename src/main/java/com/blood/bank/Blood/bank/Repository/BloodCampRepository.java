package com.blood.bank.Blood.bank.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blood.bank.Blood.bank.model.BloodCamp;

@Repository
public interface BloodCampRepository extends JpaRepository<BloodCamp, Integer> {}

