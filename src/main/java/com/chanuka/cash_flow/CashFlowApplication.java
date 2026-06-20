package com.chanuka.cash_flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CashFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashFlowApplication.class, args);
	}

}
