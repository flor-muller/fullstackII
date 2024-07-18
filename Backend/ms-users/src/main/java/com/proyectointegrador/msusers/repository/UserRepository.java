package com.proyectointegrador.msusers.repository;

import com.proyectointegrador.msusers.domain.Ticket;
import com.proyectointegrador.msusers.domain.User;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepository implements IUserRepository{

    private final Keycloak keycloak;

    private final TicketRepository ticketRepository;

    @Value("${backend.keycloak.realm}")
    private String realm;

    private User toUser(UserRepresentation userRepresentation) {
        List<Ticket> tickets = null;
        try {
            tickets = ticketRepository.findByUserId(userRepresentation.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new User(userRepresentation.getId(), userRepresentation.getUsername(), userRepresentation.getFirstName(), userRepresentation.getLastName(), userRepresentation.getEmail(), tickets);
    }

    @Override
    public List<User> findUserByUsername(String username) {
        List<UserRepresentation> userRepresentation = keycloak
                .realm(realm)
                .users()
                .search(username);
        return userRepresentation.stream().map(this::toUser).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findUserById(String id) {
        UserRepresentation userRepresentation = keycloak.realm(realm)
                .users()
                .get(id)
                .toRepresentation();
        return Optional.of(toUser(userRepresentation));
    }

    @Override
    public List<User> findAll() {
        return keycloak.realm(realm).users()
                .list()
                .stream()
                .map(this::toUser)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(String id) {
        keycloak.realm(realm).users().delete(id);
    }

    @Override
    public User updateUser(User user) {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserRepresentation userRepresentation = usersResource.get(user.getId()).toRepresentation();
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setUsername(user.getUserName());
        usersResource.get(user.getId()).update(userRepresentation);
        return user;
    }

    public List<String> getUserRoles(String userId) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        List<RoleRepresentation> userRoles = usersResource.get(userId).roles().realmLevel().listAll();
        List<String> roles = userRoles.stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
        return roles;
    }

    @Override
    public List<String> addRoleToUser(String id, String role) {
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(id);
        RoleMappingResource roleMappingResource = userResource.roles();
        RoleRepresentation roleRepresentation = realmResource.roles().get(role).toRepresentation();
        roleMappingResource.realmLevel().add(Collections.singletonList(roleRepresentation));
        List<RoleRepresentation> roleRepresentations = userResource.roles().realmLevel().listAll();
        List<String> roles = roleRepresentations.stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
        return roles;
    }

    @Override
    public void deleteRoleToUser(String id, String role) {
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(id);
        RoleMappingResource roleMappingResource = userResource.roles();
        RoleRepresentation roleRepresentation = realmResource.roles().get(role).toRepresentation();
        roleMappingResource.realmLevel().remove(Collections.singletonList(roleRepresentation));
    }
}
