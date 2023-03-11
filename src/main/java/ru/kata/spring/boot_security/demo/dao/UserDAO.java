package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.Authentication;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> getListUser();
    User getUserById(int id);
    Optional<User> findByUserName(String userName);
    void addUser(User user);
    void update(User user);
    void delete(int id);
    String getRoleSetToString(Authentication authentication);
}
