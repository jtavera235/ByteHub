package com.example.ErrorLogAPI;

import com.example.ErrorLogAPI.models.User;
import com.example.ErrorLogAPI.repositories.UserRepository;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ErrorLogApiApplication implements CommandLineRunner {



	public static void main(String[] args) {
		SpringApplication.run(ErrorLogApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
