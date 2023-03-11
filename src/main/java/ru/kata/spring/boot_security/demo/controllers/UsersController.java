package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.servises.UserService;

/**Класс-контролер для пользователя. В нем описываются все необходимые методы и маппинг.
 * Имеется зависимость на UserService.
 */
@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    /**Метод запускается с адреса /account
     * Получает имя(логин) от зарегестрированного пользователя и по нему ищет пользователя в бд.
     * Далее отправляет этого пользователя на представление users/show которое выводит на экран всю информацию о нем.
     */
    @GetMapping("")
    public String show(Model model, Authentication authentication) {
        model.addAttribute("thisUser", authentication);
        model.addAttribute("thisUserRoles", userService.getRoleSetToString(authentication).trim());
        model.addAttribute("userByUsername", userService.loadUserByUsername(authentication.getName()));
        return "users/index-bootstrap";
    }
}
