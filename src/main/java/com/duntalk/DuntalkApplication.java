package com.duntalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DuntalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuntalkApplication.class, args);
	}

}
