package com.blood.bank.Blood.bank.service;

import com.blood.bank.Blood.bank.Repository.DonationRepository;
import com.blood.bank.Blood.bank.model.Donation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;

    public Donation recordDonation(Donation donation) {
        return donationRepository.save(donation);
    }

    public List<Donation> getDonationsByDonor(Long donorId) {
        return donationRepository.findByDonorId(donorId);
    }

    public Optional<Donation> getLastDonationByDonor(Long donorId) {
        return donationRepository.findByDonorId(donorId).stream()
                .max((d1, d2) -> d1.getDonationDate().compareTo(d2.getDonationDate()));
    }

    public boolean isDonorEligible(Long donorId) {
        Optional<Donation> lastDonation = getLastDonationByDonor(donorId);
        if (lastDonation.isPresent()) {
            LocalDate lastDonationDate = lastDonation.get().getDonationDate();
            // Assuming a donor can donate every 90 days (approx. 3 months)
            return lastDonationDate.plusDays(90).isBefore(LocalDate.now());
        }
        // If no previous donations, donor is eligible
        return true;
    }
}