package test.buzanov.accountmanager.dto.converter;

import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.entity.Account;

/**
 * Интерфейс конвертера для сущности Account.
 * @author Aleksey Buzanov
 */

public interface IAccountDtoConverter {
    Account toAccountEntity(final AccountDto accountDto);

    AccountDto toAccountDTO(final Account account);
}
