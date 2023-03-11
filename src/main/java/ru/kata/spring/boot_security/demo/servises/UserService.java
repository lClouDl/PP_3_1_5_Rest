package ru.kata.spring.boot_security.demo.servises;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getListUser();
    User getUserById(int id);
    void addUser(User user);
    void update(User user);
    void delete(int id);
    String getRoleSetToString(Authentication authentication);
}
