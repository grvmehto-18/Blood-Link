package com.blood.bank.Blood.bank.service;

import com.blood.bank.Blood.bank.Repository.AddressRepository;
import com.blood.bank.Blood.bank.Repository.DonorRepository;
import com.blood.bank.Blood.bank.exception.DonorNotFoundException;
import com.blood.bank.Blood.bank.model.Address;
import com.blood.bank.Blood.bank.model.Donor;
import com.blood.bank.Blood.bank.dto.DonorRegistrationDto;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.blood.bank.Blood.bank.dto.DonorRegistrationDto;
import com.blood.bank.Blood.bank.mapper.DonorMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Service
public class DonorService {

    private final DonorRepository donorRepository;
    private final AddressRepository addressRepository;
    private final DonorMapper donorMapper;
    private final PasswordEncoder passwordEncoder;

    public DonorService(DonorRepository donorRepository, AddressRepository addressRepository, DonorMapper donorMapper, PasswordEncoder passwordEncoder) {
        this.donorRepository = donorRepository;
        this.addressRepository = addressRepository;
        this.donorMapper = donorMapper;
        this.passwordEncoder = passwordEncoder;
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
                log.error("Error parsing lastDonateDate: {}", lastDonateDateStr, e);
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

        

        log.info("Searching donors with: bloodGroup={}, country={}, state={}, district={}, city={}, lastDonateDate={}, gender={}, maxBirthDate={}, minBirthDate={}",
                bloodGroup, country, state, district, city, lastDonateDate, gender, maxBirthDate, minBirthDate);
        String finalGender = (gender != null && gender.isEmpty()) ? null : gender;

        String finalCountry = (country != null && country.isEmpty()) ? null : country;
        String finalState = (state != null && state.isEmpty()) ? null : state;
        String finalDistrict = (district != null && district.isEmpty()) ? null : district;
        String finalCity = (city != null && city.isEmpty()) ? null : city;

        List<Donor> donors = donorRepository.searchDonors(
                bloodGroup,
                finalCountry,
                finalState,
                finalDistrict,
                finalCity,
                lastDonateDate,
                finalGender,
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
    public void updateDonor(Long id, DonorRegistrationDto updatedDonorDto) {
        Donor existingDonor = donorRepository.findById(id)
                .orElseThrow(() -> new DonorNotFoundException("Donor not found with id: " + id));

        if (updatedDonorDto.getPassword() != null && !updatedDonorDto.getPassword().isEmpty()) {
            existingDonor.setPassword(passwordEncoder.encode(updatedDonorDto.getPassword()));
        }
        donorMapper.updateDonorFromDto(updatedDonorDto, existingDonor);

        Address addressToUpdate;
        if (existingDonor.getAddresses().isEmpty()) {
            addressToUpdate = new Address();
            addressToUpdate.setDonor(existingDonor);
            existingDonor.getAddresses().add(addressToUpdate);
        } else {
            addressToUpdate = existingDonor.getAddresses().iterator().next();
        }

        addressToUpdate.setCountry(updatedDonorDto.getCountry());
        addressToUpdate.setState(updatedDonorDto.getState());
        addressToUpdate.setDistrict(updatedDonorDto.getDistrict());
        addressToUpdate.setCity(updatedDonorDto.getCity());
        addressToUpdate.setStreetAddress(updatedDonorDto.getStreetAddress());
        addressToUpdate.setZipCode(updatedDonorDto.getZipCode());
        addressToUpdate.setLandmark(updatedDonorDto.getLandmark());

        donorRepository.save(existingDonor);
        addressRepository.save(addressToUpdate);
    }

    public Optional<DonorRegistrationDto> getDonorDtoById(Long id) {
        return donorRepository.findById(id).map(donorMapper::toDto);
    }

    public List<String> findDistinctCountries() {
        List<String> countries = addressRepository.findDistinctCountries();
        log.info("Found distinct countries: {}", countries);
        return countries;
    }

    public List<String> findDistinctStatesByCountry(String country) {
        List<String> states = addressRepository.findDistinctStatesByCountry(country);
        log.info("Found distinct states for country {}: {}", country, states);
        return states;
    }

    public List<String> findDistinctDistrictsByCountryAndState(String country, String state) {
        List<String> districts = addressRepository.findDistinctDistrictsByCountryAndState(country, state);
        log.info("Found distinct districts for country {} and state {}: {}", country, state, districts);
        return districts;
    }

    public List<String> findDistinctCitiesByCountryAndStateAndDistrict(String country, String state, String district) {
        List<String> cities = addressRepository.findDistinctCitiesByCountryAndStateAndDistrict(country, state, district);
        log.info("Found distinct cities for country {}, state {}, district {}: {}", country, state, district, cities);
        return cities;
    }

    public List<String> findAllDistinctStates() {
        List<String> states = addressRepository.findAllDistinctStates();
        log.info("Found all distinct states: {}", states);
        return states;
    }

    public List<String> findAllDistinctDistricts() {
        List<String> districts = addressRepository.findAllDistinctDistricts();
        log.info("Found all distinct districts: {}", districts);
        return districts;
    }

    public List<String> findAllDistinctCities() {
        List<String> cities = addressRepository.findAllDistinctCities();
        log.info("Found all distinct cities: {}", cities);
        return cities;
    }
}

