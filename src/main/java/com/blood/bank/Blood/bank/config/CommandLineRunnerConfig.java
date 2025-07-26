package com.blood.bank.Blood.bank.config;

import com.blood.bank.Blood.bank.Repository.DonorRepository;
import com.blood.bank.Blood.bank.Repository.RoleRepository;
import com.blood.bank.Blood.bank.model.Address;
import com.blood.bank.Blood.bank.model.Donor;
import com.blood.bank.Blood.bank.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class CommandLineRunnerConfig {

    @Bean
    public CommandLineRunner init(RoleRepository roleRepo, DonorRepository donorRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if they don't exist
            Role userRole = roleRepo.findByName("ROLE_USER").orElseGet(() -> roleRepo.save(new Role(null, "ROLE_USER")));
            Role adminRole = roleRepo.findByName("ROLE_ADMIN").orElseGet(() -> roleRepo.save(new Role(null, "ROLE_ADMIN")));

//            // Create default admin user if not exists using environment variables
//            String adminEmail = System.getenv("ADMIN_EMAIL");
//            String adminPassword = System.getenv("ADMIN_PASSWORD");
//            String adminName = System.getenv("ADMIN_NAME");
//            String adminPhone = System.getenv("ADMIN_PHONE");
//
//            if (adminEmail != null && adminPassword != null && donorRepo.findByEmail(adminEmail).isEmpty()) {
//                Donor admin = new Donor();
//                admin.setDateOfBirth(Date.valueOf("2000-01-01"));
//                admin.setGender("Male");
//                admin.setBloodGroup("0+ve");
//                admin.setUsername(adminEmail);
//                admin.setEmail(adminEmail);
//                admin.setPassword(passwordEncoder.encode(adminPassword));
//                admin.setFullName(adminName);
//                admin.setDateOfBirth(Date.valueOf("2000-01-01"));
//                admin.setGender("Male");
//                admin.setBloodGroup("0+ve");
//                admin.setUsername(adminEmail);
//                admin.setEmail(adminEmail);
//                admin.setPassword(passwordEncoder.encode(adminPassword));
//                admin.setFullName(adminName);
//                admin.setPhoneNumber(adminPhone); // You might want to make this configurable too
//                admin.setEnabled(true);
//                admin.setRoles(Set.of(adminRole,userRole));
//
//                // Create and set a default address
//                Address defaultAddress = new Address();
//                defaultAddress.setCountry("India");
//                defaultAddress.setState("Delhi");
//                defaultAddress.setDistrict("Central Delhi");
//                defaultAddress.setCity("New Delhi");
//                defaultAddress.setStreetAddress("123 Admin St");
//                defaultAddress.setZipCode("110001");
//                defaultAddress.setLandmark("Near Admin Tower");
//                defaultAddress.setDonor(admin); // Associate address with donor
//
//                Set<Address> addresses = new HashSet<>();
//                addresses.add(defaultAddress);
//                admin.setAddresses(addresses);
//                donorRepo.save(admin);
//
//                admin.setPhoneNumber(adminPhone); // You might want to make this configurable too
//                admin.setEnabled(true);
//                admin.setRoles(Set.of(adminRole,userRole));
//
//                donorRepo.save(admin);
//            }
        };
    }
}
