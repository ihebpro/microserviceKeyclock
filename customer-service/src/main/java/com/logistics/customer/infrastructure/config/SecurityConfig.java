package com.logistics.customer.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Sécurisation du microservice Customer Service via JWT.
 * 
 * @EnableWebSecurity : obligatoire pour configurer la chaîne de filtres.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permet d'utiliser @PreAuthorize("hasRole('ADMIN')") par exemple.
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints internes ( communication inter-services via Gateway ou internal call )
                // Note : En prod, on restreindra davantage l'endpoint /internal.
                .requestMatchers("/api/v1/users/internal").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .anyRequest().authenticated()
            )
            // Activation de la validation du JWT issuer-uri (Keycloak)
            .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> {}));

        return http.build();
    }
}
