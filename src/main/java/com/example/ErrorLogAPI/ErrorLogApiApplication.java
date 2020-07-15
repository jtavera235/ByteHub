package com.example.ErrorLogAPI;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.ErrorLogAPI.controllers"})
@ComponentScan({"com.example.ErrorLogAPI.authentication"})
public class ErrorLogApiApplication implements CommandLineRunner {



	public static void main(String[] args) {
		SpringApplication.run(ErrorLogApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
