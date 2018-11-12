package com.oup.integration.sps.acad.stockreceived;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
//@Profile("dev")
public class StockReceivedDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockReceivedDataApplication.class, args);
	}
}
