package com.ead.eadnotificationhex.;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EadNotificationHexApplication {

	public static void main(String[] args) {
		SpringApplication.run(EadNotificationHexApplication.class, args);
	}

}
