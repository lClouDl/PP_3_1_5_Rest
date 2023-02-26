package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.servises.UserService;

//В этом классе я настроил провайдер аутентификации. Использует две зависимости: userService и passwordEncoder

@Component
public class AuthProviderImp implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthProviderImp(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

//Метод настройки аутентификации. Из объекта Authentication (объект, который производит вход на сайт) получает логин.
//Далее, с помощью этого логина, ищет объект в базе данных. После достает пароль в зашифрованном виде из базы.
//Сравнивает полученный из бд пароль и только что введенный. Если не сходится - возвращает ошибку, иначе же
//создает токен аутентификации.
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        UserDetails userDetails = userService.loadUserByUsername(name);
        String password = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(password, userDetails.getPassword()))
            throw new BadCredentialsException("Введен неверный пароль!");
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
