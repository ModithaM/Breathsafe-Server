package com.itp.breathsafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BreatheSafeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BreatheSafeServerApplication.class, args);
	}

}
