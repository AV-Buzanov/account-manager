package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.entity.User;

import java.util.Collection;

/**
 * Интерфейс сервиса для сущности Transaction.
 * @author Aleksey Buzanov
 */

public interface IAccountService {
    Collection<AccountDto> findAll(int page, int size, final User user) throws Exception;

    AccountDto findOne(@Nullable final String id) throws Exception;

    AccountDto create(@Nullable final AccountDto accountDto, @NotNull final User user) throws Exception;

    AccountDto update(@Nullable final AccountDto accountDto) throws Exception;

    void delete(String id) throws Exception;

    boolean addUser(@Nullable final String id, String username) throws Exception;
}
