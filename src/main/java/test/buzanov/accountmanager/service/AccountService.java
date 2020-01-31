package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.dto.converter.IAccountDtoConverter;
import test.buzanov.accountmanager.entity.Account;
import test.buzanov.accountmanager.repository.AccountRepository;
import test.buzanov.accountmanager.repository.TransactionRepository;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Класс реализует бизнесс логику и валидацию данных для сущности Account.
 * @author Aleksey Buzanov
 */

@Component
public class AccountService implements IAccountService {

    @NotNull
    private final AccountRepository accountRepository;

    @NotNull
    private final TransactionRepository transactionRepository;

    @NotNull
    private final IAccountDtoConverter accountDtoConverter;

    public AccountService(@NotNull final AccountRepository accountRepository,
                          @NotNull final TransactionRepository transactionRepository,
                          @NotNull final IAccountDtoConverter accountDtoConverter) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.accountDtoConverter = accountDtoConverter;
    }

    public Collection<AccountDto> findAll(int page, int size) {
        return accountRepository.findAll(PageRequest.of(page,size)).stream()
                .map(accountDtoConverter::toAccountDTO)
                .collect(Collectors.toList());
    }

    @Nullable
    public AccountDto findOne(@Nullable final String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        @Nullable final AccountDto accountDto = accountDtoConverter
                .toAccountDTO(accountRepository.findById(id)
                        .orElse(null));
        return accountDto;
    }

    @Nullable
    @Transactional
    public AccountDto create(@Nullable final AccountDto accountDto) throws Exception {
        if (accountDto == null || accountDto.getId() == null || accountDto.getId().isEmpty())
            throw new Exception("Argument can't be empty or null");
        if (accountRepository.existsById(accountDto.getId()))
            throw new Exception("Account already exists");
        final Account account = accountDtoConverter.toAccountEntity(accountDto);
        return accountDtoConverter.toAccountDTO(accountRepository.saveAndFlush(account));
    }

    @Nullable
    @Transactional
    public AccountDto update(@Nullable final AccountDto accountDto) throws Exception {
        if (accountDto == null || accountDto.getId() == null || accountDto.getId().isEmpty())
            throw new Exception("Argument can't be empty or null");
        final Account account = accountRepository.findById(accountDto.getId()).orElse(null);
        if (account == null)
            throw new Exception("Account not found");
        final Account convertedAccountForUpdate = accountDtoConverter.toAccountEntity(accountDto);
        convertedAccountForUpdate.setBalance(account.getBalance());
        return accountDtoConverter.toAccountDTO(accountRepository.saveAndFlush(convertedAccountForUpdate));
    }

    @Transactional
    public void delete(String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        accountRepository.deleteById(id);
    }
}
