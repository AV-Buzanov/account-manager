package test.buzanov.accountmanager.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.entity.Account;
import test.buzanov.accountmanager.form.AccountForm;

/**
 * Класс реализует перевод сущности Account в DTO объект и обратно.
 *
 * @author Aleksey Buzanov
 */

@Component
public class AccountConverter implements IAccountConverter {
    @Nullable
    public Account toAccountEntity(@Nullable final AccountForm accountForm) {
        if (accountForm == null) return null;
        @NotNull final Account account = new Account();
        account.setName(accountForm.getName());
        account.setDescription(accountForm.getDescription());
        return account;
    }

    @Nullable
    public AccountDto toAccountDTO(@Nullable final Account account) {
        if (account == null) return null;
        @NotNull final AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setBalance(account.getBalance());
        accountDto.setCreation(account.getCreation());
        accountDto.setName(account.getName());
        accountDto.setDescription(account.getDescription());
        return accountDto;
    }

    public Account updateEntity(final AccountForm accountForm, final Account account) {
        if (account == null || accountForm == null) return null;
        account.setName(accountForm.getName());
        account.setDescription(accountForm.getDescription());
        return account;
    }
}
