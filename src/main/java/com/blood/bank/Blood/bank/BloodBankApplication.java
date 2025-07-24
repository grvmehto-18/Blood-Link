package com.blood.bank.Blood.bank;

import com.blood.bank.Blood.bank.Repository.RoleRepository;
import com.blood.bank.Blood.bank.model.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableJpaAuditing
@EnableAsync
@EnableTransactionManagement
@SpringBootApplication
public class BloodBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloodBankApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(RoleRepository roleRepo) {
		return args -> {
			if (roleRepo.findByName("ROLE_USER").isEmpty()) {
				roleRepo.save(new Role(null, "ROLE_USER"));
			}
			if (roleRepo.findByName("ROLE_ADMIN").isEmpty()) {
				roleRepo.save(new Role(null, "ROLE_ADMIN"));
			}
		};
	}


}
