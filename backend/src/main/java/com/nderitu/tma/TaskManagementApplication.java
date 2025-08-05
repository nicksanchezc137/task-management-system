package com.nderitu.tma;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.nderitu.tma.seed.SeedDataService;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class TaskManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagementApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(SeedDataService seedDataService) {
		return args -> {
			seedDataService.loadSeedData();
		};
	}
}
