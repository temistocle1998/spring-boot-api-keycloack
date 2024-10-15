package tech.ec2dlt.backend.controller;

import javax.ws.rs.BadRequestException;

import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import tech.ec2dlt.backend.service.KeycloakUserService;
import org.keycloak.admin.client.Keycloak;

@RestController
@RequestMapping(path = "/api")
public class AuthController {
	private final KeycloakUserService keycloakUserService;

	public AuthController(@Lazy KeycloakUserService keycloakUserService) {
		this.keycloakUserService = keycloakUserService;
	}

	private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @GetMapping
	public ResponseEntity<String> hello() {
		return ResponseEntity.ok("Hello!");
	}

	@GetMapping(path = "/user")
	@PreAuthorize("hasRole('role_user')")
	public ResponseEntity<String> helloUser() {
		return ResponseEntity.ok("Hello From User!");
	}

	@GetMapping(path = "/admin")
	@PreAuthorize("hasRole('role_admin')")
	public ResponseEntity<String> helloAdmin() {
		return ResponseEntity.ok("Hello From Admin!");
	}

	@GetMapping(path = "/current-user")
	public ResponseEntity<String> getCurrentUser() {
		return ResponseEntity.ok("Hello From current User!");
	}

	@PostMapping(path = "login", consumes="application/json")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		Keycloak keycloak  = keycloakUserService.newKeycloakBuilderWithPasswordCredentials(loginRequest.getUsername(),
				loginRequest.getPassword()).build();


        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (BadRequestException ex) {
            LOG.warn("invalid account. User probably hasn't verified email.", ex);
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        }
	}

}
