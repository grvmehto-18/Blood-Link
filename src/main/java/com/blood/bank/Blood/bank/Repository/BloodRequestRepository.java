package com.blood.bank.Blood.bank.Repository;

import com.blood.bank.Blood.bank.model.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    List<BloodRequest> findByBloodGroupAndLocationAndStatus(String bloodGroup, String location, String status);
    List<BloodRequest> findByRequesterId(Long requesterId);
}