package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.form.AccountForm;

import java.util.Collection;

/**
 * Интерфейс сервиса для сущности Transaction.
 * @author Aleksey Buzanov
 */

public interface IAccountService {
    Collection<AccountDto> findAll(int page, int size, final User user) ;

    AccountDto findOne(@Nullable final String id, final User user);

    AccountDto create(@Nullable final AccountForm accountDto, @NotNull final User user) ;

    AccountDto update(@Nullable final AccountForm accountForm, final String id, final User user);

    void delete(String id) ;

    boolean addUser(@Nullable final String id, final String username, final User user);
}
