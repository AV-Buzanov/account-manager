package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.buzanov.accountmanager.converter.IAccountConverter;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.entity.Account;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.form.AccountForm;
import test.buzanov.accountmanager.repository.AccountRepository;
import test.buzanov.accountmanager.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс реализует бизнесс логику и валидацию данных для сущности Account.
 *
 * @author Aleksey Buzanov
 */

@Component
public class AccountService implements IAccountService {

    @NotNull
    private final AccountRepository accountRepository;

    @NotNull
    private final UserRepository userRepository;

    @NotNull
    private final IAccountConverter accountConverter;

    public AccountService(@NotNull final AccountRepository accountRepository,
                          @NotNull final UserRepository userRepository,
                          @NotNull final IAccountConverter accountConverter) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountConverter = accountConverter;
    }

    public Collection<AccountDto> findAll(int page, int size, final User user) {
        return accountRepository.findAllByUsers(PageRequest.of(page, size), user).stream()
                .map(accountConverter::toAccountDTO)
                .collect(Collectors.toList());
    }

    @Nullable
    public AccountDto findOne(@Nullable final String id, final User user) {
        if (id == null || id.isEmpty()) throw new NullPointerException("Argument can't by empty or null");
        @Nullable final AccountDto accountDto = accountConverter
                .toAccountDTO(accountRepository.findAccountByIdAndUsers(id, user)
                        .orElseThrow(() -> new EntityNotFoundException("Account not found.")));
        return accountDto;
    }

    @Nullable
    @Transactional
    public AccountDto create(@Nullable final AccountForm accountForm, @Nullable final User user) {
        final Account account = accountConverter.toAccountEntity(accountForm);
        if (account == null || user == null) throw new NullPointerException("Argument can't be empty or null");
        account.getUsers().add(user);
        return accountConverter.toAccountDTO(accountRepository.saveAndFlush(account));
    }

    @Nullable
    @Transactional
    public AccountDto update(@Nullable final AccountForm accountForm, final String id, final User user) {
        if (accountForm == null || id == null || id.isEmpty())
            throw new NullPointerException("Argument can't be empty or null");
        final Account account = accountRepository.findAccountByIdAndUsers(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Account not found."));
        final Account updatedAccount = accountConverter.updateEntity(accountForm, account);
        return accountConverter.toAccountDTO(accountRepository.saveAndFlush(updatedAccount));
    }

    @Nullable
    @Transactional
    public List<AccountDto> update(@Nullable final User user, final Date date) {
        if (user == null || date == null)
            throw new NullPointerException("Argument can't be empty or null");
        return accountRepository.findAllByUsersContainsAndUpdateIsAfter(user, date)
                .orElse(new ArrayList<>()).stream()
                .map(accountConverter::toAccountDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean addUser(@Nullable final String id, final String username, final User user) {
        if (id == null || id.isEmpty() || username == null || username.isEmpty())
            throw new NullPointerException("Argument can't be empty or null");
        final Account findedAccount = accountRepository.findAccountByIdAndUsers(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Account not found."));
        final User findedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        findedAccount.getUsers().add(findedUser);
        return accountRepository.saveAndFlush(findedAccount).getUsers().contains(findedUser);
    }

    @Transactional
    public void delete(String id) {
        if (id == null || id.isEmpty()) throw new NullPointerException("Id can't by empty or null");
        accountRepository.deleteById(id);
    }
}
