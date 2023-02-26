package ru.kata.spring.boot_security.demo.servises;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.models.User;
import java.util.Optional;

//Класс-сервис. Реализует интерфейс UserDetailsService.
//Это необходимо для иной настройки метода поиска пользователя в базе данных по логину.

@Service
public class UserValidateService implements UserDetailsService {

    private final UserDAO userDAO;

    public UserValidateService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

//В этом методе, в случае, если пользователь не найден, мы возвращаем null а не исключение.
//В таком виде, нам будет удобно использовать этот метод в валидаторе.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDAO.findByUserName(username);
        if (user.isEmpty())
            return null;
        return user.get();
    }
}
