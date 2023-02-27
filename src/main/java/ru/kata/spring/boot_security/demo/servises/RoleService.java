package ru.kata.spring.boot_security.demo.servises;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    public Role findById(int id){
        return roleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Transactional
    public void uppdate(int id, Role roleUppdate) {
        roleUppdate.setId(id);
        roleRepository.save(roleUppdate);
    }

    @Transactional
    public void delete(int id) {
        roleRepository.deleteById(id);
    }
}
