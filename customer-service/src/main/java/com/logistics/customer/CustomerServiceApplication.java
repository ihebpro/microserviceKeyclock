package com.logistics.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Point d'entrée du Customer Service.
 * 
 * Responsabilités :
 * - Gestion du référentiel des entreprises (Companies)
 * - Suivi des contrats et des niveaux de service (SLA)
 * - Exposition d'API pour la consultation des profils clients
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
