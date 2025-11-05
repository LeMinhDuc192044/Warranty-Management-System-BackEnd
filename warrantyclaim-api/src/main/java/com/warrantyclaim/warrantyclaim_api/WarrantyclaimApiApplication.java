package com.warrantyclaim.warrantyclaim_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class WarrantyclaimApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarrantyclaimApiApplication.class, args);
	}

}
