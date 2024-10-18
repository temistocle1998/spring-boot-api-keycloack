package tech.ec2dlt.backend.controller;

import javax.ws.rs.BadRequestException;

import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.springframework.web.client.RestTemplate;
import tech.ec2dlt.backend.ApiResponse;
import tech.ec2dlt.backend.service.KeycloakUserService;
import org.keycloak.admin.client.Keycloak;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin("*")
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
	public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser(@RequestHeader(name = "Authorization", required = false) String token) {
		try {
			if (token == null || !token.startsWith("Bearer ")) {
				throw new IllegalArgumentException("Invalid or missing token");
			}

			// Remove the 'Bearer ' prefix
			String authToken = token.substring(7);
			Map<String, Object> userInfo = getUserInfo(authToken);

			// Format the response
			Map<String, Object> response = new HashMap<>();
			response.put("id", userInfo.get("sub")); // User ID from Keycloak
			response.put("username", userInfo.get("preferred_username"));
			response.put("email", userInfo.get("email"));
			response.put("firstName", userInfo.get("given_name"));
			response.put("lastName", userInfo.get("family_name"));
			response.put("token", authToken);

			ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>(false, response);
			return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
		} catch (Exception e) {
			LOG.error("Unauthenticated", e);

			Map<String, Object> response = new HashMap<>();
			response.put("data", e.getMessage());
			ApiResponse<Map<String, Object>> errorResponse = new ApiResponse<>(true, response);

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}

	@PostMapping(path = "login", consumes="application/json")
	public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest loginRequest) {
		Keycloak keycloak  = keycloakUserService.newKeycloakBuilderWithPasswordCredentials(loginRequest.getUsername(),
				loginRequest.getPassword()).build();

        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();

			String token = accessTokenResponse.getToken();
			String refreshToken = accessTokenResponse.getRefreshToken(); // Get the refresh token
			long expireTime = accessTokenResponse.getExpiresIn();

			Map<String, Object> userInfo = getUserInfo(token);

			// Format the response
			Map<String, Object> response = new HashMap<>();
			response.put("id", userInfo.get("sub")); // User ID from Keycloak
			response.put("username", userInfo.get("preferred_username"));
			response.put("email", userInfo.get("email"));
			response.put("firstName", userInfo.get("given_name"));
			response.put("lastName", userInfo.get("family_name"));
			response.put("token", token);
			response.put("refresh_token", refreshToken); // Include the refresh token
			response.put("expire_token", expireTime); // Include the expire duration

			ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>(false, response);
			return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (BadRequestException ex) {
			Map<String, Object> response = new HashMap<>();
			response.put("data", "User probably hasn't verified email"); // User ID from Keycloak
			LOG.warn("Invalid account. User probably hasn't verified email.", ex);
			ApiResponse<Map<String, Object>> errorResponse = new ApiResponse<>(true, response);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
		catch (Exception e) {
			LOG.error("Login failed", e);
			Map<String, Object> response = new HashMap<>();
			response.put("data", e.getMessage());
			ApiResponse<Map<String, Object>> errorResponse = new ApiResponse<>(true, response);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}

	private Map<String, Object> getUserInfo(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<Map> response = restTemplate.exchange(
				"http://localhost:9090/realms/realm-demo/protocol/openid-connect/userinfo",
				HttpMethod.GET,
				entity,
				Map.class
		);

		return response.getBody();
	}
}
