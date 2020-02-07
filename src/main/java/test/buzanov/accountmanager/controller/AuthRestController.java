package test.buzanov.accountmanager.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import test.buzanov.accountmanager.dto.UserDto;
import test.buzanov.accountmanager.entity.Token;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.enumurated.Role;
import test.buzanov.accountmanager.service.ITokenService;
import test.buzanov.accountmanager.service.IUserService;

import java.util.Collections;

/**
 * Класс публикует REST сервис для управления сущностью Account.
 * @author Aleksey Buzanov
 */

@RestController
@RequestMapping(value = "/auth")
public class AuthRestController {

        private final IUserService userService;
    private final ITokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    public AuthRestController(@Qualifier(value = "userService") final IUserService userService, ITokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public String addUser(@RequestBody UserDto userDto) throws Exception {
        User user = new User();
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);
        user.setAuthorities(Collections.singleton(Role.USER));

        userService.create(user);

        return "Okay";
    }

    @GetMapping("/wellcome")
    public ResponseEntity<String> wellcome(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok("Authorize success "+user.getName()+" "+user.getAuthorities().toString());
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody UserDto userDto) throws Exception {
        return ResponseEntity.ok(tokenService.login(userDto));
    }
}
