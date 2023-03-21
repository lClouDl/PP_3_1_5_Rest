package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.servises.RoleService;
import ru.kata.spring.boot_security.demo.servises.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;
import ru.kata.spring.boot_security.demo.util.UserNotUpdateException;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class FirstRestController {

    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    public FirstRestController(UserService userService, RoleService roleService, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public List<UserDTO> getUsers() {
        return userService.getListUser().stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @GetMapping("/thisUser")
    public UserDTO getThisUser(Authentication authentication) {
        return convertToUserDTO((User) userService.loadUserByUsername(authentication.getName()));
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") int id) {
        return convertToUserDTO(userService.getUserById(id));
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse("Пользователь с таким id не найден!");
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField()).append(":").append(error.getDefaultMessage()).append(":");
            }
            throw new UserNotCreatedException(errorMessage.toString());
        }
        userService.addUser(convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField()).append(":").append(error.getDefaultMessage()).append(":");
            }
            throw new UserNotUpdateException(errorMessage.toString());
        }

        User user = convertToUser(userDTO);
        user.setId(id);
        userService.update(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotUpdateException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(e.getMessage());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private User convertToUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setRoleSet(new HashSet<>());
        for (String role : userDTO.getRoleSet().toUpperCase().split(" ")) {
            user.getRoleSet()
                    .add((role.equals(roleService.findById(1).getRole().substring(5)) ? roleService.findById(1) :
                            (role.equals(roleService.findById(2).getRole().substring(5)) ? roleService.findById(2) :
                                    null)));
        }
        return user;
    }
    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setRoleSet("");
        for (Role role : user.getRoleSet()) {
            userDTO.setRoleSet(userDTO.getRoleSet() + role.getRole().substring(5) + " ");
        }
        userDTO.setRoleSet(userDTO.getRoleSet().trim());
        return userDTO;
    }
}
