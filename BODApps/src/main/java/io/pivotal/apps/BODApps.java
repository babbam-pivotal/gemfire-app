package io.pivotal.apps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = {"io.pivotal.apps","io.pivotal.apps.controller","io.pivotal.apps.service"})
public class BODApps{

	public static void main(String[] args) {
		SpringApplication.run(BODApps.class, args);
	}
}
