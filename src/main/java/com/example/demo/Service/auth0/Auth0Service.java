package com.example.demo.Service.auth0;

import com.example.demo.Configuration.Auth0Properties;
import com.example.demo.DTOs.Request.Auth0UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Auth0Service implements IAuth0Service {

    private final Auth0Properties auth0Properties;
    private final RestTemplate restTemplate;

    // =======================================
    // OBTENER TOKEN DEL MANAGEMENT API
    // =======================================
    private String obtenerManagementToken() {
        String url = "https://" + auth0Properties.getDomain() + "/oauth/token";

        Map<String, String> body = new HashMap<>();
        body.put("client_id", auth0Properties.getClientId());
        body.put("client_secret", auth0Properties.getClientSecret());
        body.put("audience", auth0Properties.getManagementAudience());
        body.put("grant_type", "client_credentials");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("access_token")) {
                return (String) responseBody.get("access_token");
            }
            throw new RuntimeException("No se pudo obtener el token de Auth0");

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error al obtener token de Auth0: " + e.getMessage(), e);
        }
    }

    // =======================================
    // CREAR USUARIO
    // =======================================
    @Override
    public String crearUsuario(String email, String password) {
        String token = obtenerManagementToken();
        String url = "https://" + auth0Properties.getDomain() + "/api/v2/users";

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        body.put("connection", "Username-Password-Authentication");
        body.put("email_verified", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("user_id")) {
                return (String) responseBody.get("user_id");
            }
            throw new RuntimeException("No se pudo crear el usuario en Auth0");

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error al crear usuario en Auth0: " + e.getResponseBodyAsString(), e);
        }
    }

    // =======================================
    // ASIGNAR ROL
    // =======================================
    @Override
    public void asignarRolAUsuario(String auth0UserId, String roleId) {
        String token = obtenerManagementToken();
        String url = "https://" + auth0Properties.getDomain()
                + "/api/v2/users/" + auth0UserId + "/roles";

        Map<String, Object> body = new HashMap<>();
        body.put("roles", List.of(roleId));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(url, entity, Void.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error al asignar rol en Auth0: " + e.getResponseBodyAsString(), e);
        }
    }

    // =======================================
    // OBTENER USUARIO
    // =======================================
    @Override
    public Auth0UserDTO obtenerUsuarioPorId(String auth0UserId) {
        String token = obtenerManagementToken();
        String url = "https://" + auth0Properties.getDomain()
                + "/api/v2/users/" + auth0UserId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Auth0UserDTO> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, Auth0UserDTO.class);

            return response.getBody();

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error al obtener usuario de Auth0: " + e.getResponseBodyAsString(), e);
        }
    }
}