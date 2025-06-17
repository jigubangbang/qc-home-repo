package com.jigubangbang.qc_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class QcServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QcServiceApplication.class, args);
	}

}
