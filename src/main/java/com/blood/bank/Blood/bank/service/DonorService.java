package com.blood.bank.Blood.bank.service;

import org.springframework.stereotype.Service;

import com.blood.bank.Blood.bank.Repository.DonorRepository;
import com.blood.bank.Blood.bank.model.Donor;

import java.util.List;
import java.util.Optional;

@Service
public class DonorService {

    DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public List<Donor> findAll(){
        return donorRepository.findAll();
    }

//    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    public boolean saveDonor(Donor donorDetails){
        try{
            // donorDetails.setPassword(encoder.encode(donorDetails.getPassword()));
            donorRepository.save(donorDetails);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void updateDonor(Donor donorDetails){
        donorRepository.save(donorDetails);
    }

    public Donor findByPhoneNumber(String phoneNumber){
        Donor donor=donorRepository.findByPhoneNumber(phoneNumber);
        return donor;
    }


    public List<Donor> searchDonors(String bloodGroup, String location) {
        return donorRepository.findByBloodGroupAndAddressContainingIgnoreCase(bloodGroup, location);
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public void blockDonor(Long id) {
        Donor donor = donorRepository.findById(id).orElseThrow(() -> new RuntimeException("Donor not found"));
        donor.setEnabled(false); // Assume 'enabled' indicates if the donor is blocked
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

    public void updateDonor(Long id, Donor updatedDonor) {
        Donor donor = donorRepository.findById(id).orElseThrow(() -> new RuntimeException("Donor not found"));
        donor.setFullName(updatedDonor.getFullName());
        donor.setEmail(updatedDonor.getEmail());
        donor.setBloodGroup(updatedDonor.getBloodGroup());
        donor.setAddress(updatedDonor.getAddress());
        donorRepository.save(donor);
    }

}

