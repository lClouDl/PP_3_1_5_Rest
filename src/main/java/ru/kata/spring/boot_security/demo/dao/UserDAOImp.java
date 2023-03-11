package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

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
        return entityManager.find(User.class, id);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return entityManager.createQuery("select u from User u where u.login = :userName", User.class)
                .setParameter("userName", userName).getResultList().stream().findFirst();
    }

    @Override
    public void addUser(User user) {
        user.setRoleSet(new HashSet<>());
        user.getRoleSet().add(entityManager.find(Role.class, 1));

        entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        entityManager.find(User.class, user.getId()).setFirstName(user.getFirstName());
        entityManager.find(User.class, user.getId()).setLastName(user.getLastName());
        entityManager.find(User.class, user.getId()).setGender(user.getGender());
        entityManager.find(User.class, user.getId()).setLogin(user.getLogin());
        entityManager.find(User.class, user.getId()).setPassword(user.getPassword());
        entityManager.find(User.class, user.getId()).setRoleSet(user.getRoleSet());

    }

    @Override
    public void delete(int id) {
        entityManager.remove(entityManager.find(User.class, id));
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
