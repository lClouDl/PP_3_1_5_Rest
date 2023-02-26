package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.servises.UserService;

//Класс-контролер для администратора. В нем описываются все необходимые методы и маппинг. Имеется зависимость на UserService.

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

//Метод запускается с пустого адреса. Получает список user'ов из бд и выводит их в виде таблицы.
    @GetMapping()
    public String index(Model model){
        model.addAttribute("users", userService.getListUser());
        return "admin/index";
    }

//Метод запускается с адреса /{id} где id - это первичный ключ в бд.
//Выводит на экран информацию о пользователе, чей id был указан в адресе
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("userById", userService.getUserById(id));
        return "admin/show";
    }

//Метод запускается с адреса /addRoleAdmin/{id} где id - это первичный ключ в бд.
//Добовляет права администратора пользователю, чей id указан в адрессе
    @GetMapping("/addRoleAdmin/{id}")
    public String addRoleAdmin(@PathVariable("id") int id, Model model) {
        userService.setAdminRole(userService.getUserById(id));
        model.addAttribute("userById", userService.getUserById(id));
        return "admin/show";
    }

//Метод запускается с адреса /removeRoleAdmin/{id} где id - это первичный ключ в бд.
//Лишает прав администратора пользователя, чей id указан в адрессе
    @GetMapping("/removeRoleAdmin/{id}")
    public String removeRoleAdmin(@PathVariable("id") int id, Model model) {
        userService.removeAdminRole(userService.getUserById(id));
        model.addAttribute("userById", userService.getUserById(id));
        return "admin/show";
    }

//Метод запускается с адреса /new Берет новый бин User и передает его на POST запрос
    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "admin/new";
    }

//Метод срабатывает при нажатии кнопки "Сохранить" на представлении new
//Если нет ошибок, при заполнении формы на представлении new,
//заполняет поля User данными из формы и добавляет этого User в бд
    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/new";

        userService.addUser(user);
        return "redirect:/admin";
    }

//Метод запускается с адреса /{id}/edit где id - это первичный ключ в бд.
//Берет пользователя из бд по id и передает его в PATCH запрос
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/edit";
    }

//Метод срабатывает при нажатии кнопки "Изменить" на представлении edit
//Если нет ошибок, при заполнении формы на представлении edit,
//обновляет User в бд согласно данным из формы
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/edit";

        userService.update(user);
        return "redirect:/admin";
    }

//Метод срабатывает при нажатии кнопки "Удалить" на представлении show
//Удаляет пользователя с id указанным в запросе из бд
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }

}
