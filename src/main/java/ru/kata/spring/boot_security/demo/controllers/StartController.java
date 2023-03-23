package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**Класс-контролер для неверефицированного пользователя. В нем описываются все необходимые методы и маппинг.
 * Имеется зависимость на UserValidator - необходим для проверки: существует ли такой пользователь в бд или нет.
 * Так же имеет зависимость на RegistrationService - осуществляет регистрацию новых пользователей.
 */
@Controller
public class StartController {

    /**Метод запускается с пустого адреса. Запускает представление
     * index (стартовую страницу) для незарегестрированных пользователей.
     */
    @GetMapping()
    public String getStart(){
        return "redirect:/login";
    }
}
