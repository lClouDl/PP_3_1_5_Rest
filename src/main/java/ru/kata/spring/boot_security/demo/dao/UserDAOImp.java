package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Класс DAO слоя. Работает через EntityManager. С предыдущей задачи добавил только два метода
 * setAdminRole(User user) и removeAdminRole(User user)
 */
@Repository
public class UserDAOImp implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> getListUser() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    public User getUserById(int id) {
        return Optional.ofNullable(entityManager.find(User.class, id)).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return entityManager.createQuery("select u from User u where u.login = :userName", User.class)
                .setParameter("userName", userName).getResultList().stream().findFirst();
    }

    @Override
    public void addUser(User user) {
        if (user.getRoleSet() == null) {
            user.setRoleSet(new HashSet<>());
            user.getRoleSet().add(entityManager.find(Role.class, 1));
        }

        entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        User updateUser = entityManager.find(User.class, user.getId());
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setGender(user.getGender());
        updateUser.setLogin(user.getLogin());
        if (!updateUser.getPassword().equals(user.getPassword())) {
            updateUser.setPassword(user.getPassword());
        }
        updateUser.setRoleSet(user.getRoleSet());
    }

    @Override
    public void delete(int id) {
        User user = Optional.ofNullable(entityManager.find(User.class, id)).orElseThrow(UserNotFoundException::new);
        entityManager.remove(user);
    }

    /**
     * Метод выводит в строку, через пробел, все роли данного пользователя
     * Этот метод необходим, для удобного заполнения navbar в html
     */
    @Override
    public String getRoleSetToString(Authentication authentication) {
        StringBuilder roleThisUser = new StringBuilder();
        for (String str : AuthorityUtils.authorityListToSet(authentication.getAuthorities())) {
            if (str.equals("ROLE_ADMIN")) {
                roleThisUser.insert(0, str.substring(5) + " ");
            } else {
                roleThisUser.append(str.substring(5)).append(" ");
            }
        }
        return roleThisUser.toString();
    }
}
