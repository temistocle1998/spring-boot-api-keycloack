package tech.ec2dlt.backend.config;

import java.util.HashMap;
import java.util.Map;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
            .serverUrl("http://localhost:9090")
            .realm("realm-demo")
            .clientId("demo")
            .username("admin")
            .password("password")
            .build();
    }
    
}
