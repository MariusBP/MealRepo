package com.mealapp.experiment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mealapp")
public class MealAppExperimentApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealAppExperimentApplication.class, args);
	}

}
