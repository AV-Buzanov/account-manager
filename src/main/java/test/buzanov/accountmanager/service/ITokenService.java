package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.dto.UserDto;
import test.buzanov.accountmanager.entity.Token;
import test.buzanov.accountmanager.entity.User;

import java.util.Collection;

/**
 * Интерфейс сервиса для сущности Transaction.
 * @author Aleksey Buzanov
 */

public interface ITokenService {

    Token login(@Nullable final UserDto user) throws Exception;

}
