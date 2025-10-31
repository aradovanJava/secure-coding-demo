package com.example.securecoding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Glavna aplikacijska klasa za Secure Coding Demo
 * 
 * Ova demo aplikacija pokazuje razliku između ranjive i sigurne 
 * implementacije validacije podataka u Spring Boot aplikaciji.
 */
@SpringBootApplication
public class SecureCodingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureCodingDemoApplication.class, args);
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║     SECURE CODING DEMO APLIKACIJA POKRENUTA!          ║");
        System.out.println("║                                                        ║");
        System.out.println("║  Otvori browser: http://localhost:8080                ║");
        System.out.println("║                                                        ║");
        System.out.println("║  RANJIVA verzija:  http://localhost:8080/vulnerable  ║");
        System.out.println("║  SIGURNA verzija:  http://localhost:8080/secure      ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
    }
}
