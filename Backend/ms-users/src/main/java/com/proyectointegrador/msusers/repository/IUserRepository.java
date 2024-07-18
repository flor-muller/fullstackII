package com.proyectointegrador.msusers.repository;

import com.proyectointegrador.msusers.domain.User;
import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    List<User> findAll();
    List<User> findUserByUsername(String username);
    Optional<User> findUserById(String id);
    void deleteUserById(String id);
    User updateUser(User user);
    List<String> getUserRoles(String id);
    List<String> addRoleToUser(String id, String role);
    void deleteRoleToUser(String id, String role);
}
