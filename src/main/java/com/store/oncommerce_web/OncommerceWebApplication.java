package com.store.oncommerce_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OncommerceWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(OncommerceWebApplication.class, args);
	}

}
