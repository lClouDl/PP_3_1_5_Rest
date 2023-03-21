package ru.kata.spring.boot_security.demo.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDTO {

    private int id;

    @NotNull(message = "Нельзя добавить человека без имени")
    @Size(min = 2, max = 20, message = "Имя должно содержать не менее 2 и не более 20 символов")
    private String firstName;

    @NotNull(message = "Нельзя добавить человека без фамилии")
    @Size(min = 1, max = 40, message = "Фамилия должна содержать не менее 1 и не более 40 символов")
    private String lastName;

    @NotNull(message = "Укажите пол человка")
    @NotEmpty(message = "Укажите пол человка")
    private String gender;

    /**Это поле содершит множество ролей для одного пользователя
     * Настроил отношение многие ко многим, указал внешний ключ.
     * Для корректной работы отказался от ленивой загрузки и настраиваем каскадность
     */

    @NotEmpty(message = "Укажите роль")
    private String roleSet;

    /**Поле для уникального логина
     */
    @NotNull(message = "Установите логин")
    @Size(min = 3, message = "Логин должен содержать больше 3 символов")
    private String login;

    /**Поле для пароля
     */
    @NotNull(message = "Установите пароль")
    @Size(min = 4, message = "Пароль должен содержать больше 4 символов")
    private String password;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    public String getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(String roleSet) {
        this.roleSet = roleSet;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
