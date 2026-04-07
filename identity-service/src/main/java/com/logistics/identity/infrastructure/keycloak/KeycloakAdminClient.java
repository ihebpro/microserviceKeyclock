package com.logistics.identity.infrastructure.keycloak;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Client HTTP pour communiquer avec l'API Admin de Keycloak.
 *
 * Ce composant encapsule toutes les interactions avec Keycloak :
 *   - Obtenir un token admin (client_credentials)
 *   - Créer un utilisateur dans un realm
 *   - Assigner un rôle à un utilisateur
 *
 * Il utilise le RestTemplate standard (synchrone) pour simplifier
 * la gestion côté service.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakAdminClient {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    /**
     * Authentifie un utilisateur auprès de Keycloak et récupère ses tokens.
     *
     * @param username Le nom d'utilisateur ou email
     * @param password Le mot de passe
     * @return Map contenant les tokens (access_token, refresh_token, etc.)
     */
    public Map<String, Object> authenticateUser(String username, String password) {
        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", username);
        formData.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        return (Map<String, Object>) response.getBody();
    }

    /**
     * Obtient un token d'accès administrateur depuis Keycloak.
     * Ce token est utilisé pour pouvoir appeler l'API Admin REST de Keycloak.
     *
     * @return access_token sous forme de String
     */
    public String getAdminToken() {
        String tokenUrl = serverUrl + "/realms/master/protocol/openid-connect/token";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", "admin-cli");
        formData.add("username", adminUsername);
        formData.add("password", adminPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        return (String) response.getBody().get("access_token");
    }

    /**
     * Crée un utilisateur dans le realm Keycloak configuré.
     *
     * @param username  Nom d'utilisateur unique
     * @param email     Email
     * @param firstName Prénom
     * @param lastName  Nom
     * @param password  Mot de passe
     * @return keycloakUserId
     */
    public String createUser(String username, String email, String firstName, String lastName, String password) {
        String adminToken = getAdminToken();
        String createUserUrl = serverUrl + "/admin/realms/" + realm + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        Map<String, Object> userPayload = Map.of(
                "username", username,
                "email", email,
                "firstName", firstName,
                "lastName", lastName,
                "enabled", true,
                "emailVerified", true,
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", password,
                        "temporary", false
                ))
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(createUserUrl, request, Void.class);

        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        String keycloakUserId = location.substring(location.lastIndexOf("/") + 1);
        log.info("Utilisateur Keycloak créé avec ID: {}", keycloakUserId);
        return keycloakUserId;
    }

    /**
     * Assigne un rôle à un utilisateur dans Keycloak.
     *
     * @param userId   ID de l'utilisateur Keycloak (sub)
     * @param roleName Nom du rôle (SELLER, WAREHOUSE, DRIVER, etc.)
     */
    public void assignRoleToUser(String userId, String roleName) {
        String adminToken = getAdminToken();
        log.info("Assignation du rôle {} à l'utilisateur Keycloak {}", roleName, userId);

        // 1. Récupérer l'objet rôle depuis Keycloak pour avoir son ID interne
        String getRoleUrl = serverUrl + "/admin/realms/" + realm + "/roles/" + roleName;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        
        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        ResponseEntity<Map> roleResponse = restTemplate.exchange(getRoleUrl, HttpMethod.GET, getRequest, Map.class);
        Map<String, Object> roleData = roleResponse.getBody();

        // 2. Mapper le rôle à l'utilisateur
        String mappingUrl = serverUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";
        HttpEntity<List<Map<String, Object>>> mappingRequest = new HttpEntity<>(List.of(roleData), headers);
        
        restTemplate.postForLocation(mappingUrl, mappingRequest);
        log.info("Rôle {} assigné avec succès.", roleName);
    }
}
