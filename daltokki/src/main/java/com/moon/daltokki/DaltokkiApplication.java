package com.moon.daltokki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DaltokkiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaltokkiApplication.class, args);
	}

}
