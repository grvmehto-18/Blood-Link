package com.blood.bank.Blood.bank.service;

import com.blood.bank.Blood.bank.Repository.AddressRepository;
import com.blood.bank.Blood.bank.Repository.DonorRepository;
import com.blood.bank.Blood.bank.exception.DonorNotFoundException;
import com.blood.bank.Blood.bank.model.Address;
import com.blood.bank.Blood.bank.model.Donor;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DonorService {

    private final DonorRepository donorRepository;
    private final AddressRepository addressRepository;

    public DonorService(DonorRepository donorRepository, AddressRepository addressRepository) {
        this.donorRepository = donorRepository;
        this.addressRepository = addressRepository;
    }

    public List<Donor> findAll() {
        return donorRepository.findAll();
    }

    public boolean saveDonor(Donor donorDetails) {
        try {
            donorRepository.save(donorDetails);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Donor findByPhoneNumber(String phoneNumber) {
        return donorRepository.findByPhoneNumber(phoneNumber);
    }

    public List<Donor> searchDonors(String bloodGroup,
                                   String country,
                                   String state,
                                   String district,
                                   String city,
                                   String lastDonateDateStr,
                                   String availability,
                                   String gender,
                                   Integer minAge,
                                   Integer maxAge) {
        java.sql.Date lastDonateDate = null;
        if (lastDonateDateStr != null && !lastDonateDateStr.isEmpty()) {
            try {
                lastDonateDate = java.sql.Date.valueOf(lastDonateDateStr);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        java.sql.Date maxBirthDate = null;
        if (minAge != null) {
            maxBirthDate = new java.sql.Date(System.currentTimeMillis() - (minAge * 365L * 24 * 60 * 60 * 1000));
        }

        java.sql.Date minBirthDate = null;
        if (maxAge != null) {
            minBirthDate = new java.sql.Date(System.currentTimeMillis() - (maxAge * 365L * 24 * 60 * 60 * 1000));
        }

        List<Donor> donors = donorRepository.searchDonors(
                bloodGroup,
                country,
                state,
                district,
                city,
                lastDonateDate,
                gender,
                maxBirthDate,
                minBirthDate
        );

        if (availability != null && !availability.isEmpty()) {
            final long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
            final long MIN_DONATION_INTERVAL_MILLIS = 90 * MILLIS_IN_DAY;

            long currentTimeMillis = System.currentTimeMillis();

            donors = donors.stream().filter(donor -> {
                if (donor.getLastDonateDate() == null) {
                    return true;
                }
                long lastDonateTimeMillis = donor.getLastDonateDate().getTime();
                long timeSinceLastDonation = currentTimeMillis - lastDonateTimeMillis;
                switch (availability) {
                    case "availableNow":
                        return timeSinceLastDonation >= MIN_DONATION_INTERVAL_MILLIS;
                    case "within24Hours":
                        return (timeSinceLastDonation >= (MIN_DONATION_INTERVAL_MILLIS - MILLIS_IN_DAY));
                    case "within3Days":
                        return (timeSinceLastDonation >= (MIN_DONATION_INTERVAL_MILLIS - (3 * MILLIS_IN_DAY)));
                    case "withinWeek":
                        return (timeSinceLastDonation >= (MIN_DONATION_INTERVAL_MILLIS - (7 * MILLIS_IN_DAY)));
                    default:
                        return true;
                }
            }).collect(Collectors.toList());
        }

        return donors;
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public void blockDonor(Long id) {
        Donor donor = donorRepository.findById(id).orElseThrow(() -> new RuntimeException("Donor not found"));
        donor.setEnabled(false);
        donorRepository.save(donor);
    }

    public void unblockDonor(Long id) {
        Donor donor = donorRepository.findById(id).orElseThrow(() -> new RuntimeException("Donor not found"));
        donor.setEnabled(true);
        donorRepository.save(donor);
    }

    public void deleteDonor(Long id) {
        donorRepository.deleteById(id);
    }

    public Optional<Donor> getDonorById(Long id) {
        return donorRepository.findById(id);
    }

    @Transactional
    public void updateDonor(Long id, Donor updatedDonorData) {
        Donor existingDonor = donorRepository.findById(id)
                .orElseThrow(() -> new DonorNotFoundException("Donor not found with id: " + id));

        existingDonor.setFullName(updatedDonorData.getFullName());
        existingDonor.setGender(updatedDonorData.getGender());
        existingDonor.setDateOfBirth(updatedDonorData.getDateOfBirth());
        existingDonor.setBloodGroup(updatedDonorData.getBloodGroup());
        existingDonor.setPhoneNumber(updatedDonorData.getPhoneNumber());
        existingDonor.setEmail(updatedDonorData.getEmail());
        existingDonor.setOccupation(updatedDonorData.getOccupation());
        existingDonor.setLastDonateDate(updatedDonorData.getLastDonateDate());
        existingDonor.setHasDiseases(updatedDonorData.isHasDiseases());
        existingDonor.setHasAllergies(updatedDonorData.isHasAllergies());
        existingDonor.setHasCardiacConditions(updatedDonorData.isHasCardiacConditions());
        existingDonor.setHasBleedingDisorders(updatedDonorData.isHasBleedingDisorders());
        existingDonor.setHasHIV(updatedDonorData.isHasHIV());

        Address addressToUpdate;
        if (existingDonor.getAddresses().isEmpty()) {
            addressToUpdate = new Address();
            addressToUpdate.setDonor(existingDonor);
            existingDonor.getAddresses().add(addressToUpdate);
        } else {
            addressToUpdate = existingDonor.getAddresses().iterator().next();
        }

        // Update address fields
        if (!updatedDonorData.getAddresses().isEmpty()) {
            Address newAddressData = updatedDonorData.getAddresses().iterator().next();
            addressToUpdate.setCountry(newAddressData.getCountry());
            addressToUpdate.setState(newAddressData.getState());
            addressToUpdate.setDistrict(newAddressData.getDistrict());
            addressToUpdate.setCity(newAddressData.getCity());
            addressToUpdate.setStreetAddress(newAddressData.getStreetAddress());
            addressToUpdate.setZipCode(newAddressData.getZipCode());
            addressToUpdate.setLandmark(newAddressData.getLandmark());
        }

        donorRepository.save(existingDonor);
        addressRepository.save(addressToUpdate);
    }

    public List<String> findDistinctCountries() {
        return addressRepository.findDistinctCountries();
    }

    public List<String> findDistinctStatesByCountry(String country) {
        return addressRepository.findDistinctStatesByCountry(country);
    }

    public List<String> findDistinctDistrictsByCountryAndState(String country, String state) {
        return addressRepository.findDistinctDistrictsByCountryAndState(country, state);
    }

    public List<String> findDistinctCitiesByCountryAndStateAndDistrict(String country, String state, String district) {
        return addressRepository.findDistinctCitiesByCountryAndStateAndDistrict(country, state, district);
    }
}

