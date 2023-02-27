package ru.kata.spring.boot_security.demo.configs;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**Этот класс служит для настройки вывода необходимого представления после регистрации,
 * с учетом прав доступа пользователя.
 */
@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {

    /**Если роль аутентифицированного пользователя соответствует "ROLE_ADMIN", значит у этого пользователя
     * права администратора, и его следует переадресовать на адрес /admin, в противном случае нужно вывести
     * адресс /user/account которая отобразит информацию о пользователе.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/admin");
        } else {
            httpServletResponse.sendRedirect("/users/account");
        }
    }
}