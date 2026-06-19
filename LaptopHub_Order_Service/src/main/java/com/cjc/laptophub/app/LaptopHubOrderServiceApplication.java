package com.cjc.laptophub.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class LaptopHubOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaptopHubOrderServiceApplication.class, args);
	}

	@Bean
	public RestTemplate rt1()
	{
		return new RestTemplate();
	}
	
}
