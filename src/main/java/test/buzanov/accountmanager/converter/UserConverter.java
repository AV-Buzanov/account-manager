package test.buzanov.accountmanager.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.dto.UserDto;
import test.buzanov.accountmanager.entity.Account;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.form.AccountForm;
import test.buzanov.accountmanager.form.UserForm;

/**
 * Класс реализует перевод сущности Account в DTO объект и обратно.
 * @author Aleksey Buzanov
 */

@Component
public class UserConverter implements IUserConverter {
    @Nullable
    public User toUserEntity(@Nullable final UserForm userForm) {
        if (userForm == null) return null;
        @NotNull final User user = new User();
        user.setName(userForm.getName());
        user.setUsername(userForm.getUsername());
        user.setPassword(userForm.getPassword());
        return user;
    }

    @Nullable
    public UserDto toUserDTO(@Nullable final User user) {
        if (user == null) return null;
        @NotNull final UserDto userDto = new UserDto();
        userDto.setCreation(user.getCreation());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setAuthorities(user.getAuthorities());
        return userDto;
    }
}
