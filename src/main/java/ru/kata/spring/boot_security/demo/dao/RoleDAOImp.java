package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;

@Repository
public class RoleDAOImp implements RoleDAO {
    private final EntityManager entityManager;

    public RoleDAOImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Role> findAll() {
        return entityManager.createQuery("select r from Role r", Role.class).getResultList();
    }

    @Override
    public Role findById(int id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public void save(Role role) {
        role.setUserSet(new HashSet<>());
        entityManager.persist(role);
    }

    @Override
    public void update(int id, Role roleUpdate) {
        entityManager.find(Role.class, id).setRole(roleUpdate.getRole());
        entityManager.find(Role.class, id).setUserSet(roleUpdate.getUserSet());
    }

    @Override
    public void delete(int id) {
        entityManager.remove(entityManager.find(Role.class, id));
    }
}
