package ru.kata.spring.boot_security.demo.models;

import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.Set;

//Класс-сущность. Хранит в бд роли, необходимые для авторизации. На данном этапе их две USER и ADMIN
//Реализует интерфейс GrantedAuthority

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String role;

//Настраил отношение многие ко многим, указал внешний ключ.
//Для корректной работы отказался от ленивой загрузки
    @ManyToMany(mappedBy = "roleSet", fetch = FetchType.EAGER)
    private Set<User> userSet;

    public Role(){}

    public Role(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<User> getUserSet() { return userSet; }

    public void setUserSet(Set<User> userSet) { this.userSet = userSet; }

    @Override
    public String toString() {
        return "id=" + id +
                ", role='" + role + '\'' +
                ", userSet=" + userSet.toString() +
                '}';
    }

    //Метод который возвращает роль.
    @Override
    public String getAuthority() {
        return getRole();
    }
}
