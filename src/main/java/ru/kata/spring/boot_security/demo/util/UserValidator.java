package ru.kata.spring.boot_security.demo.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.servises.UserValidateService;

    /** Класс-валидатор. Реализует интерфейс Validator. Имеет зависимость от UserValidateService.
    * Следит за тем, чтобы в бд не попало два одиноковых логина.
    */
@Component
public class UserValidator implements Validator {

    private final UserValidateService userValidateService;

    public UserValidator(UserValidateService userValidateService) {
        this.userValidateService = userValidateService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    /**Метод выполняет в случае, когда loadUserByUsername() вернет что-либо кроме null,
     * выдаст ошибку с пояснением, что пользователь с таким логином уже существует в бд.
     */
    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userValidateService.loadUserByUsername(user.getUsername()) != null)
            errors.rejectValue("login", "", "Человек, с таким логином уже существует.");
    }
}
