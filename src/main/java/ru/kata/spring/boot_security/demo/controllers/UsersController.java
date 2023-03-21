package ru.kata.spring.boot_security.demo.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**Класс-контролер для пользователя.
 */
@Controller
@RequestMapping("/users")
public class UsersController {
    @GetMapping("")
    public String show() {
        return "users/index-bootstrap";
    }
}
