package com.sbd.bazartemtudo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BazartemtudoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BazartemtudoApplication.class, args);
	}
	
}
