package com.logistics.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée du Gateway Service.
 *
 * Ce service joue le rôle de "frontal" unique de la plateforme :
 * - Il reçoit toutes les requêtes HTTP clientes
 * - Il valide le JWT émis par Keycloak (via spring-security OAuth2 resource server)
 * - Il route la requête vers le bon microservice via Consul load-balancer (lb://)
 * - Il applique les circuit breakers Resilience4j sur chaque route
 *
 * La configuration de routing est externalisée dans application.yml.
 */
@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }


}
