package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.kata.spring.boot_security.demo.security.AuthProviderImp;

/**Конфигурационный класс, который настраивает секьюрность. Имеет две зависимости:
 * 1. SuccessUserHandler - необходима для определения правильного отображения представления после аутентификации,
 * 2. AuthProviderImp - определяет процесс аутентификации
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;

    private final AuthProviderImp authProviderImp;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, AuthProviderImp authProviderImp) {
        this.successUserHandler = successUserHandler;
        this.authProviderImp = authProviderImp;
    }

    /**Настройка обработки запросов: адрес:
     * "/users/account" доступен пользователям с правами USER и ADMIN
     * адреса: "/", "/auth/registration", "/error" доступны незарегестрированным пользователям
     * остальные адреса доступны только пользователям с правами ADMIN
     * далее идет настройка определения представления после аутентификации и авторизации
     * и в конце настройка адресса выхода(logout)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/users", "/users/index-bootstrap").hasAnyRole("USER")
                .antMatchers("/", "/auth/registration", "/error").permitAll()
                .anyRequest().hasRole("ADMIN")
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/")
                .permitAll();
    }

/**В этом методе настраивается процесс аутентификации.
 * В данном случае с помощью провайдера (authenticationProvider())
 */
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProviderImp);
    }

}