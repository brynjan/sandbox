package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
public class ProductResourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductResourceApplication.class, args);
	}

}
