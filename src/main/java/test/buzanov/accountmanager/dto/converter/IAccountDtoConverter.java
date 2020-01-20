package test.buzanov.accountmanager.dto.converter;

import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.entity.Account;

public interface IAccountDtoConverter {
    Account toAccountEntity(final AccountDto accountDto);

    AccountDto toAccountDTO(final Account account);
}
