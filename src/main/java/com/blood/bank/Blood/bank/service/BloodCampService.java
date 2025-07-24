package com.blood.bank.Blood.bank.service;

import com.blood.bank.Blood.bank.Repository.BloodCampRepository;
import com.blood.bank.Blood.bank.model.BloodCamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BloodCampService {

    private final BloodCampRepository bloodCampRepository;

    public List<BloodCamp> getAllBloodCamps() {
        return bloodCampRepository.findAll();
    }

    public Optional<BloodCamp> getBloodCampById(Long id) {
        return bloodCampRepository.findById(id);
    }

    public BloodCamp saveBloodCamp(BloodCamp bloodCamp) {
        return bloodCampRepository.save(bloodCamp);
    }

    public void deleteBloodCamp(Long id) {
        bloodCampRepository.deleteById(id);
    }
}