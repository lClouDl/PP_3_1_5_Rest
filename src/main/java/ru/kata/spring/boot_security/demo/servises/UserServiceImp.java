package ru.kata.spring.boot_security.demo.servises;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

/**Класс-сервис. Осуществляет основные CRUD операции. Реализует интерфейс UserService, к которому добавил наследование
 * от UserDetailsService. С предыдущей задачи, добавилось три метода.
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getListUser() {
        return userDAO.getListUser();
    }

    @Override
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    @Override
    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.addUser(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        user.setPassword((passwordEncoder.matches(user.getPassword(), userDAO.getUserById(user.getId()).getPassword()))
                ? userDAO.getUserById(user.getId()).getPassword() : passwordEncoder.encode(user.getPassword()));
        userDAO.update(user);
    }

    @Override
    @Transactional
    public void delete(int id) {
        userDAO.delete(id);
    }

    public String getRoleSetToString(Authentication authentication) {
        return userDAO.getRoleSetToString(authentication);
    }

    /**Метод, который переопределен из интерфейса UserDetailsService. Осуществляет проверку,
     * имеется ли пользователь с таким логинов, в базе данных
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDAO.findByUserName(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("Логин введен неверно!");
        return user.get();
    }
}
