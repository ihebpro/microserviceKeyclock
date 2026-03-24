package com.logistics.identity.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration de sécurité du Identity Service.
 *
 * Stratégie :
 * - Stateless (pas de sessions HTTP) : authentification uniquement via JWT
 * - /api/v1/auth/** : accessible SANS token et ignoré par les filtres de
 * sécurité
 * - Tout le reste : requiert un JWT valide émis par Keycloak
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Ignore les endpoints d'authentification pour éviter toute validation JWT
     * inutile.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**",
                "/swagger-ui.html", "/error");
    }

    /**
     * Configure la chaîne de filtres de sécurité Spring Security.
     *
     * @param http L'objet HttpSecurity configuré par Spring
     * @return SecurityFilterChain configuré
     * @throws Exception Si la configuration est invalide
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactive CSRF – inutile en mode stateless REST API
                .csrf(csrf -> csrf.disable())

                // Session stateless : pas de HttpSession côté serveur
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Redondance sur les routes publiques par précaution
                        .requestMatchers("/api/v1/auth/**", "/error").permitAll()
                        // Health check Consul accessible sans auth
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        // Tout le reste nécessite un JWT valide
                        .anyRequest().authenticated())

                // Activation du validation JWT via Keycloak
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                }));

        return http.build();
    }

    /**
     * Bean RestTemplate utilisé par le KeycloakAdminClient pour
     * les appels HTTP vers l'Admin REST API de Keycloak.
     *
     * @return RestTemplate instance Spring managée
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
