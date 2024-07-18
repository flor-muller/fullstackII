package com.proyectointegrador.msusers.service;

import com.proyectointegrador.msusers.domain.User;
import com.proyectointegrador.msusers.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository iUserRepository;

    public List<User> findAll() {
        return iUserRepository.findAll();
    }

    public List<User> findUserByUserName(String userName) {
        return iUserRepository.findUserByUsername(userName);
    }

    public Optional<User> findUserById(String id) {
        return iUserRepository.findUserById(id);
    }

    public void deleteUserById(String id) {
        iUserRepository.deleteUserById(id);
    }

    public User updateUser(User user) {
        return iUserRepository.updateUser(user);
    }

    public List<String> getUserRoles(String id) {
        return iUserRepository.getUserRoles(id);
    }

    public List<String> addRoleToUser(String id, String role) {
        return iUserRepository.addRoleToUser(id, role);
    }

    public void deleteRoleToUser(String id, String role) {
        iUserRepository.deleteRoleToUser(id, role);
    }
}
