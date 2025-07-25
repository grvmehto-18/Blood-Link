package com.blood.bank.Blood.bank.config;

import com.blood.bank.Blood.bank.Repository.DonorRepository;
import com.blood.bank.Blood.bank.Repository.RoleRepository;
import com.blood.bank.Blood.bank.model.Donor;
import com.blood.bank.Blood.bank.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class CommandLineRunnerConfig {

    @Bean
    public CommandLineRunner init(RoleRepository roleRepo, DonorRepository donorRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if they don't exist
            Role userRole = roleRepo.findByName("ROLE_USER").orElseGet(() -> roleRepo.save(new Role(null, "ROLE_USER")));
            Role adminRole = roleRepo.findByName("ROLE_ADMIN").orElseGet(() -> roleRepo.save(new Role(null, "ROLE_ADMIN")));

            // Create default admin user if not exists using environment variables
            String adminEmail = System.getenv("ADMIN_EMAIL");
            String adminPassword = System.getenv("ADMIN_PASSWORD");
            String adminName = System.getenv("ADMIN_NAME");
            String adminPhone = System.getenv("ADMIN_PHONE");

            if (adminEmail != null && adminPassword != null && donorRepo.findByEmail(adminEmail).isEmpty()) {
                Donor admin = new Donor();
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setFullName(adminName);
                admin.setPhoneNumber(adminPassword); // You might want to make this configurable too
                admin.setEnabled(true);
                admin.setRoles(Set.of(adminRole,userRole));
                donorRepo.save(admin);
            }
        };
    }
}
