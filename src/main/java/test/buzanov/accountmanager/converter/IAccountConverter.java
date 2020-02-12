package test.buzanov.accountmanager.converter;

import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.entity.Account;
import test.buzanov.accountmanager.form.AccountForm;

/**
 * Интерфейс конвертера для сущности Account.
 * @author Aleksey Buzanov
 */

public interface IAccountConverter {
    Account toAccountEntity(final AccountForm accountForm);

    Account updateEntity(final AccountForm accountForm, final Account account);

    AccountDto toAccountDTO(final Account account);
}
