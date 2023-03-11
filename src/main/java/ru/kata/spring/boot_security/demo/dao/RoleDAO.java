package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleDAO {
    List<Role> findAll();
    Role findById(int id);
    void save(Role role);
    void update(int id, Role roleUpdate);
    void delete(int id);
}
