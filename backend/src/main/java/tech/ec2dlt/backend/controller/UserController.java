package tech.ec2dlt.backend.controller;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import tech.ec2dlt.backend.service.KeycloakUserService;

import java.util.List;

@RestController
@RequestMapping(path="/api/users")
public class UserController {
    private final KeycloakUserService keycloakUserService;

    public UserController(@Lazy KeycloakUserService keycloakUserService) {
        this.keycloakUserService = keycloakUserService;
    }



    @GetMapping
    @PreAuthorize("hasRole('role_admin')")
    public ResponseEntity<List<UserRepresentation>> getAllUsers() {
        List<UserRepresentation> users = keycloakUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping(path="/{id}")
    @PreAuthorize("hasRole('role_admin')")
    public ResponseEntity<UserRepresentation> getUserById(@PathVariable String id) {
        UserRepresentation user = keycloakUserService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping(path="/{id}")
    @PreAuthorize("hasRole('role_admin')")
    public ResponseEntity<Void> updateUser(@PathVariable String id, @RequestBody UserRepresentation userRepresentation) {
        userRepresentation.setId(id);
        keycloakUserService.updateUser(userRepresentation);
        return ResponseEntity.noContent().build();
    }
}