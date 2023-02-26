package ru.kata.spring.boot_security.demo.servises;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

//Класс-сервис. Осуществляет основные CRUD операции. Реализует интерфейс UserService, к которому добавил наследование
//от UserDetailsService. С предыдущей задачи, добавилось три метода.

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.update(user);
    }

    @Override
    @Transactional
    public void delete(int id) {
        userDAO.delete(id);
    }

    //Метод добавления прав администратора
    @Override
    @Transactional
    public void setAdminRole(User user) {
        userDAO.setAdminRole(user);
    }

    //Метод лишения прав администратора
    @Override
    @Transactional
    public void removeAdminRole(User user) {
        userDAO.removeAdminRole(user);
    }

//Метод, который переопределен из интерфейса UserDetailsService. Осуществляет проверку,
//имеется ли пользователь с таким логинов, в базе данных
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDAO.findByUserName(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("Логин введен неверно!");
        return user.get();
    }
}
