package test.buzanov.accountmanager.converter;

import test.buzanov.accountmanager.dto.UserDto;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.form.UserForm;

/**
 * Интерфейс конвертера для сущности Account.
 *
 * @author Aleksey Buzanov
 */

public interface IUserConverter {
    User toUserEntity(final UserForm userForm);

    UserDto toUserDTO(final User user);
}
