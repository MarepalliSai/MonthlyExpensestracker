package com.ecentum.MonthlyExpenses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MonthlyExpensesApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(MonthlyExpensesApplication.class, args);
	}

}
