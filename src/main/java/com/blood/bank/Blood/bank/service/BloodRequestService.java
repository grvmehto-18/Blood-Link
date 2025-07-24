package com.blood.bank.Blood.bank.service;

import com.blood.bank.Blood.bank.Repository.BloodRequestRepository;
import com.blood.bank.Blood.bank.Repository.DonorRepository;
import com.blood.bank.Blood.bank.model.BloodRequest;
import com.blood.bank.Blood.bank.model.Donor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodRequestService {

    private final BloodRequestRepository bloodRequestRepository;
    private final DonorRepository donorRepository;

    public BloodRequest createBloodRequest(BloodRequest bloodRequest) {
        bloodRequest.setStatus("Pending");
        bloodRequest.setRequestDate(LocalDateTime.now());
        return bloodRequestRepository.save(bloodRequest);
    }

    public List<BloodRequest> getAllBloodRequests() {
        return bloodRequestRepository.findAll();
    }

    public BloodRequest getBloodRequestById(Long id) {
        return bloodRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blood Request not found"));
    }

    public BloodRequest updateBloodRequestStatus(Long id, String status) {
        BloodRequest bloodRequest = getBloodRequestById(id);
        bloodRequest.setStatus(status);
        if ("Fulfilled".equals(status)) {
            bloodRequest.setFulfilledDate(LocalDateTime.now());
        }
        return bloodRequestRepository.save(bloodRequest);
    }

    public List<Donor> findMatchingDonors(String bloodGroup, String location) {
        // This will use the updated findByBloodGroupAndAddressContainingIgnoreCase method
        return donorRepository.findByBloodGroupAndAddressContainingIgnoreCase(bloodGroup, location);
    }

    public List<BloodRequest> getBloodRequestsByRequester(Long requesterId) {
        return bloodRequestRepository.findByRequesterId(requesterId);
    }
}