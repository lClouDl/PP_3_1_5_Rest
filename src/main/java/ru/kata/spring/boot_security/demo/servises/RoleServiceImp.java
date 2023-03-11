package ru.kata.spring.boot_security.demo.servises;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleServiceImp implements RoleService {

    private final RoleDAO roleDAO;

    public RoleServiceImp(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public List<Role> findAll(){
        return roleDAO.findAll();
    }

    @Override
    public Role findById(int id){
        return roleDAO.findById(id);
    }

    @Override
    @Transactional
    public void save(Role role) {
        roleDAO.save(role);
    }

    @Override
    @Transactional
    public void update(int id, Role roleUpdate) {
        roleDAO.update(id, roleUpdate);
    }

    @Override
    @Transactional
    public void delete(int id) {
        roleDAO.delete(id);
    }
}
