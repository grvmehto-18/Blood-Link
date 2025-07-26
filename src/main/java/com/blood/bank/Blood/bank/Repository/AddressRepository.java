package com.blood.bank.Blood.bank.Repository;

import com.blood.bank.Blood.bank.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT DISTINCT a.country FROM Address a WHERE a.country IS NOT NULL")
    List<String> findDistinctCountries();

    @Query("SELECT DISTINCT a.state FROM Address a WHERE a.country = :country AND a.state IS NOT NULL")
    List<String> findDistinctStatesByCountry(@Param("country") String country);

    @Query("SELECT DISTINCT a.district FROM Address a WHERE a.country = :country AND a.state = :state AND a.district IS NOT NULL")
    List<String> findDistinctDistrictsByCountryAndState(@Param("country") String country, @Param("state") String state);

    @Query("SELECT DISTINCT a.city FROM Address a WHERE a.country = :country AND a.state = :state AND a.district = :district AND a.city IS NOT NULL")
    List<String> findDistinctCitiesByCountryAndStateAndDistrict(@Param("country") String country, @Param("state") String state, @Param("district") String district);

    @Query("SELECT DISTINCT a.state FROM Address a WHERE a.state IS NOT NULL")
    List<String> findAllDistinctStates();

    @Query("SELECT DISTINCT a.district FROM Address a WHERE a.district IS NOT NULL")
    List<String> findAllDistinctDistricts();

    @Query("SELECT DISTINCT a.city FROM Address a WHERE a.city IS NOT NULL")
    List<String> findAllDistinctCities();
}
