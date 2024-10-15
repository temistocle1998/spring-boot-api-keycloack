package tech.ec2dlt.backend.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

@Service
public class KeycloakUserService {
	private final Keycloak keycloak;
	private final String realm = "realm-demo"; // Le realm à utiliser

	public KeycloakUserService(Keycloak keycloak) {
		this.keycloak = keycloak;
	}

	// Méthode pour récupérer tous les utilisateurs

	public List<UserRepresentation> getAllUsers() {
		try {
			UsersResource usersResource = keycloak.realm(realm).users();
			return usersResource.list(); // This should return a list of users
		} catch (NotAuthorizedException e) {
			// Handle not authorized error
			System.err.println("Not authorized to fetch users: " + e.getMessage());
		} catch (NotFoundException e) {
			// Handle not found error
			System.err.println("Realm not found: " + e.getMessage());
		} catch (Exception e) {
			// Handle other exceptions
			System.err.println("Error fetching users: " + e.getMessage());
		}
		return Collections.emptyList(); // Return an empty list in case of error
	}

	// Méthode pour récupérer un utilisateur par ID
	public UserRepresentation getUserById(String userId) {
		return keycloak.realm(realm).users().get(userId).toRepresentation();
	}

	// Méthode pour mettre à jour un utilisateur
	public void updateUser(UserRepresentation userRepresentation) {
		keycloak.realm(realm).users().get(userRepresentation.getId()).update(userRepresentation);
	}


	public KeycloakBuilder newKeycloakBuilderWithPasswordCredentials(String username, String password) {
		return KeycloakBuilder.builder() //
				.realm(realm) //
				.serverUrl("http://localhost:9090")
				.clientId("demo")
				.username(username) //
				.password(password);
	}
}
