package com.loanconnect.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the LoanConnect backend.
 * Run with: mvn spring-boot:run
 * The API will be available at http://localhost:8080
 */
@SpringBootApplication
public class LoanConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanConnectApplication.class, args);
    }
}
