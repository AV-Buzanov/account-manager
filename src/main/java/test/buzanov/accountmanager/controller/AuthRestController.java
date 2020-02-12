package test.buzanov.accountmanager.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import test.buzanov.accountmanager.dto.UserDto;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.enumurated.Role;
import test.buzanov.accountmanager.form.UserForm;
import test.buzanov.accountmanager.service.IUserService;

import java.util.Collections;

/**
 * Класс публикует REST сервис для управления сущностью Account.
 *
 * @author Aleksey Buzanov
 */

@RestController
@RequestMapping(value = "/auth")
public class AuthRestController {

    private final IUserService userService;


    private final PasswordEncoder passwordEncoder;

    public AuthRestController(@Qualifier(value = "userService") final IUserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;

        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> addUser(@RequestBody UserForm userDto) throws Exception {
        User user = new User();
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);
        user.setAuthorities(Collections.singleton(Role.USER));

        userService.create(user);

        return ResponseEntity.ok("Registration successful");
    }
}
