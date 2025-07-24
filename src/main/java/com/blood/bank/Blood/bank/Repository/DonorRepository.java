package com.blood.bank.Blood.bank.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blood.bank.Blood.bank.model.Donor;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {

   Optional<Donor> findByEmail(String email);
   Optional<Donor> findByVerificationCode(String verificationCode);
   Optional<Donor> findByResetToken(String resetToken);


//    List<Donor> findDonorByBloodGroupAndLocation(String bloodGroup,String city);
   public Donor findByPhoneNumber(String phoneNumber);
   public List<Donor> findByBloodGroupAndAddressContainingIgnoreCase(String bloodGroup,String address);
}

