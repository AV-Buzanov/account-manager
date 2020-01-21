package test.buzanov.accountmanager.dto.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.entity.Account;

/**
 * Класс реализует перевод сущности Account в DTO объект и обратно.
 * @author Aleksey Buzanov
 */

@Component
public class AccountDtoConverter implements IAccountDtoConverter {
    @Nullable
    public Account toAccountEntity(@Nullable final AccountDto accountDto) {
        if (accountDto == null || accountDto.getId() == null) return null;
        @NotNull final Account account = new Account();
        account.setId(accountDto.getId());
        return account;
    }

    @Nullable
    public AccountDto toAccountDTO(@Nullable final Account account) {
        if (account == null) return null;
        @NotNull final AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setBalance(account.getBalance());
        accountDto.setCreation(account.getCreation());
        return accountDto;
    }
}
