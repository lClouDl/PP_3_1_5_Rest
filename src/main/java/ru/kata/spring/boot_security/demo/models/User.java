package ru.kata.spring.boot_security.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

/**Класс-сущность. Хранит в бд данные о пользователях. Реализует интерфейс UserDetails
 * С предыдущей задачи добавилось несколько полей (roleSet, login, password) и методов.
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Нельзя добавить человека без имени")
    @NotEmpty(message = "Нельзя добавить человека без имени")
    @Size(min = 2, max = 20, message = "Имя должно содержать не менее 2 и не более 20 символов")
    @Column(name = "firstName")
    private String firstName;

    @NotNull(message = "Нельзя добавить человека без фамилии")
    @NotEmpty(message = "Нельзя добавить человека без фамилии")
    @Size(min = 1, max = 40, message = "Фамилия должна содержать не менее 1 и не более 40 символов")
    @Column(name = "lastName")
    private String lastName;

    @NotNull(message = "Укажите пол человка")
    @NotEmpty(message = "Укажите пол человка")
    @Size(min = 1, message = "Укажите пол человка")
    @Column(name = "gender")
    private String gender;

    /**Это поле содершит множество ролей для одного пользователя
     * Настроил отношение многие ко многим, указал внешний ключ.
     * Для корректной работы отказался от ленивой загрузки и настраиваем каскадность
     */
    @ManyToMany()
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private Set<Role> roleSet;

    /**Поле для уникального логина
     */
    @NotNull(message = "Установите логин")
    @NotEmpty(message = "Установите логин")
    @Size(min = 3, message = "Логин должен содержать больше 3 символов")
    @Column(name = "login")
    private String login;

    /**Поле для пароля
     */
    @NotNull(message = "Установите пароль")
    @NotEmpty(message = "Установите пароль")
    @Size(min = 4, message = "Пароль должен содержать больше 4 символов")
    @Column(name = "password")
    private String password;


    public User(){}

    public User(String firstName, String lastName, String gender, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Set<Role> getRoleSet() { return roleSet; }

    public void setRoleSet(Set<Role> roleSet) { this.roleSet = roleSet; }

    /**Метод переопределн из интерфейса UserDetails, необходим для авторизации. Возвращает список ролей.
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoleSet();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**Далее идут методы из интерфейса UserDetails необходимые для дополнительной настройки сущности
     */
    @JsonIgnore
    @Override
    public String getUsername() {
        return getLogin();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.getId()
                && firstName.equals(user.getFirstName())
                && lastName.equals(user.getLastName())
                && gender.equals(user.getGender());
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, gender);
    }
}
