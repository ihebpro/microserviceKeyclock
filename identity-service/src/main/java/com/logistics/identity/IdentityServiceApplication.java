package com.logistics.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Point d'entrée du Identity Service.
 *
 * Responsabilités :
 *   - Créer des comptes utilisateurs dans Keycloak via l'Admin REST API
 *   - Maintenir une représentation locale des utilisateurs (AppUser) avec leurs rôles métier
 *   - Orchestrer l'inscription : création Keycloak + notification au Customer Service
 *
 * @EnableDiscoveryClient : s'enregistre auprès de Consul pour être découvrable
 * @EnableFeignClients    : active les clients Feign pour communiquer avec Customer Service
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class IdentityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityServiceApplication.class, args);
    }
}
