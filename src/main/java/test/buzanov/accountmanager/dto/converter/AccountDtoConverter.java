package test.buzanov.accountmanager.dto.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.entity.Account;

@Component
public class AccountDtoConverter {
    @Nullable
    public Account toAccountEntity(@Nullable final AccountDto accountDto) {
        if (accountDto == null) return null;
        @NotNull final Account account = new Account();
        account.setId(accountDto.getId());
        account.setBalance(accountDto.getBalance());
        return account;
    }

    @Nullable
    public AccountDto toAccountDTO(@Nullable final Account account) {
        if (account == null) return null;
        @NotNull final AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setBalance(account.getBalance());
        return accountDto;
    }
}
