package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.servises.RoleService;
import ru.kata.spring.boot_security.demo.servises.UserService;
import javax.validation.Valid;

/**Класс-контролер для администратора. В нем описываются все необходимые методы и маппинг.
 * Имеется зависимость на UserService.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

/**Метод запускается с пустого адреса. Получает список user'ов из бд и выводит их в виде таблицы.*/
    @GetMapping()
    public String index(@ModelAttribute("user") User user, Model model, Authentication authentication){
        model.addAttribute("thisUserRoles", userService.getRoleSetToString(authentication).trim());
        model.addAttribute("thisUser", authentication);
        model.addAttribute("users", userService.getListUser());
        model.addAttribute("userByUsername", userService.loadUserByUsername(authentication.getName()));
        model.addAttribute("addRoles", roleService.findAll()); //РАЗОБРАТЬСЯ!!
        return "admin/index-bootstrap";
    }

/**Метод срабатывает при нажатии кнопки "Сохранить" на представлении new
 * Если нет ошибок, при заполнении формы на представлении new,
 * заполняет поля User данными из формы и добавляет этого User в бд
 */
    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "redirect:/admin";

        userService.addUser(user);
        return "redirect:/admin";
    }

/**Метод срабатывает при нажатии кнопки "Изменить" на представлении edit
 * Если нет ошибок, при заполнении формы на представлении edit,
 * обновляет User в бд согласно данным из формы
 */
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "redirect:/admin";

        userService.update(user);
        return "redirect:/admin";
    }

/**Метод срабатывает при нажатии кнопки "Удалить" на представлении show
 * Удаляет пользователя с id указанным в запросе из бд
 */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }

}
