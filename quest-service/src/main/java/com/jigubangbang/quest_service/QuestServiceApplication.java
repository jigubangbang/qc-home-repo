package com.jigubangbang.quest_service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.jigubangbang.quest_service.repository")
public class QuestServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuestServiceApplication.class, args);
	}

}
