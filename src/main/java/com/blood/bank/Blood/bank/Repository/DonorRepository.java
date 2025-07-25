package com.blood.bank.Blood.bank.Repository;


import com.blood.bank.Blood.bank.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {

   Optional<Donor> findByEmail(String email);
   Optional<Donor> findByVerificationCode(String verificationCode);
   Optional<Donor> findByResetToken(String resetToken);


//    List<Donor> findDonorByBloodGroupAndLocation(String bloodGroup,String city);
   public Donor findByPhoneNumber(String phoneNumber);

   @Query("SELECT d FROM Donor d LEFT JOIN d.addresses a WHERE " +
           "(:bloodGroup IS NULL OR d.bloodGroup = :bloodGroup) AND " +
           "(:country IS NULL OR a.country = :country) AND " +
           "(:state IS NULL OR a.state = :state) AND " +
           "(:district IS NULL OR a.district = :district) AND " +
           "(:city IS NULL OR a.city = :city) AND " +
           "(:lastDonateDate IS NULL OR d.lastDonateDate >= :lastDonateDate) AND " +
           "(:gender IS NULL OR d.gender = :gender) AND " +
           "(cast(:maxBirthDate as date) IS NULL OR d.dateOfBirth <= :maxBirthDate) AND " +
           "(cast(:minBirthDate as date) IS NULL OR d.dateOfBirth >= :minBirthDate)")
   List<Donor> searchDonors(
           @Param("bloodGroup") String bloodGroup,
           @Param("country") String country,
           @Param("state") String state,
           @Param("district") String district,
           @Param("city") String city,
           @Param("lastDonateDate") Date lastDonateDate,
           @Param("gender") String gender,
           @Param("maxBirthDate") Date maxBirthDate,
           @Param("minBirthDate") Date minBirthDate
   );

}

