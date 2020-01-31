package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.dto.AccountDto;

import java.util.Collection;

/**
 * Интерфейс сервиса для сущности Transaction.
 * @author Aleksey Buzanov
 */

public interface IAccountService {
    Collection<AccountDto> findAll(int page, int size);

    AccountDto findOne(@Nullable final String id) throws Exception;

    AccountDto create(@Nullable final AccountDto accountDto) throws Exception;

    AccountDto update(@Nullable final AccountDto accountDto) throws Exception;

    void delete(String id) throws Exception;
}
