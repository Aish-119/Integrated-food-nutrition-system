package com.nutrition.nutrition_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication(scanBasePackages = "com.nutrition")
@EntityScan(basePackages = "com.nutrition.nutrition.entity")
@EnableJpaRepositories(basePackages = "com.nutrition.nutrition.repository")
public class NutritionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NutritionServiceApplication.class, args);
	}
}