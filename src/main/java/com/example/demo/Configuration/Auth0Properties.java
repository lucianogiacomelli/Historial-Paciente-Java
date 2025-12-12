package com.example.demo.Configuration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "auth0")
public class Auth0Properties {

    private String audience;
    private String domain;

    private String clientId;
    private String clientSecret;

    private String managementAudience;

    private String roleAdmin;
    private String roleMedico;
    private String roleRecepcionista;
}