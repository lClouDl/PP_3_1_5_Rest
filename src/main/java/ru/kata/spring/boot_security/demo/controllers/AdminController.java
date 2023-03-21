package ru.kata.spring.boot_security.demo.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**Класс-контролер для администратора.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * Метод запускается с пустого адреса. Получает список user'ов из бд и выводит их в виде таблицы.
     */
    @GetMapping()
    public String index() {
        return "admin/index-bootstrap";
    }
}