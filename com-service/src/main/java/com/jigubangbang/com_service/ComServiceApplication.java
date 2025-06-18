package com.jigubangbang.com_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ComServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComServiceApplication.class, args);
	}

}
