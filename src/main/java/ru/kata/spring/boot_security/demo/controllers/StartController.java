package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.servises.RegistrationService;
import ru.kata.spring.boot_security.demo.util.UserValidator;
import javax.validation.Valid;

//Класс-контролер для неверефицированного пользователя. В нем описываются все необходимые методы и маппинг.
//Имеется зависимость на UserValidator - необходим для проверки: существует ли такой пользователь в бд или нет.
//Так же имеет зависимость на RegistrationService - осуществляет регистрацию новых пользователей.

@Controller
public class StartController {

    private final UserValidator userValidator;
    private final RegistrationService registrationService;

    public StartController(UserValidator userValidator, RegistrationService registrationService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }

//Метод запускается с пустого адреса. Запускает представление
//index (стартовую страницу) для незарегестрированных пользователей.
    @GetMapping()
    public String getStart(){
        return "index";
    }

//Метод запускается с адреса auth/registration
//Передает новый бин User в POST запрос
    @GetMapping("/auth/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

//Метод запускается при нажатии кнопки "Зарегестрировать" на представлении registration
//Если все поля были введены без ошибок, заполняет поля бина User данными из формы
//и регестрирует его в бд как нового пользователя.
    @PostMapping("/auth/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult){
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return "auth/registration";
        registrationService.register(user);
        return "redirect:/login";
    }

}
