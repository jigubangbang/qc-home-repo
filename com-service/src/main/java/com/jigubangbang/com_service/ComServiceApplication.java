package com.jigubangbang.com_service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.jigubangbang.com_service.repository")
public class ComServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComServiceApplication.class, args);
	}

}
