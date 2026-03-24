package com.logistics.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Point d'entrée du Config Service.
 *
 * @EnableConfigServer : active le serveur de configuration Spring Cloud.
 * Ce service lit les fichiers YAML depuis le filesystem local (ou Git)
 * et les expose aux autres microservices via HTTP.
 *
 * Les autres services se connectent à ce serveur au démarrage pour
 * récupérer leur configuration spécifique (datasource, ports, etc.).
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }
}
