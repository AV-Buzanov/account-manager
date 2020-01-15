package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.dto.converter.TransactionDtoConverter;
import test.buzanov.accountmanager.entity.Account;
import test.buzanov.accountmanager.entity.Transaction;
import test.buzanov.accountmanager.repository.AccountRepository;
import test.buzanov.accountmanager.repository.TransactionRepository;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @NotNull
    @Autowired
    private TransactionRepository transactionRepository;

    @NotNull
    @Autowired
    private AccountRepository accountRepository;


    private Lock lock = new ReentrantLock();

    @NotNull
    @Autowired
    private TransactionDtoConverter transactionDtoConverter;

    public Collection<TransactionDto> findAll() {
        return transactionRepository.findAll().stream().map(transactionDtoConverter::toTransactionDTO).collect(Collectors.toList());
    }

    public Collection<TransactionDto> findAllByAccount(String id) {
        return transactionRepository.findAllByAccountId(id).stream().map(transactionDtoConverter::toTransactionDTO).collect(Collectors.toList());
    }

    @Nullable
    public TransactionDto findOne(@Nullable final String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        @Nullable final TransactionDto accountDto = transactionDtoConverter.toTransactionDTO(transactionRepository.findById(id).orElse(null));
        return accountDto;
    }

    @Nullable
    public TransactionDto create(@Nullable final TransactionDto transactionDto) throws Exception {
        if (transactionDto == null || transactionDto.getId() == null || transactionDto.getId().isEmpty()
                || transactionDto.getAccountId() == null || transactionDto.getAccountId().isEmpty() ||
                transactionDto.getSum() == 0)
            throw new Exception("Argument can't be empty or null");
        if (transactionRepository.existsById(transactionDto.getId()))
            throw new Exception("Transaction id already exists");
        lock.tryLock(20000, TimeUnit.MILLISECONDS);
        try {
            withdraw(transactionDto);
        } finally {
            lock.unlock();
        }
        return transactionDto;
    }

    @Nullable
    @Transactional
    public synchronized TransactionDto update(@Nullable final TransactionDto accountDto) throws Exception {
        if (accountDto == null || accountDto.getId() == null || accountDto.getId().isEmpty())
            throw new Exception("Argument can't be empty or null");
        if (!transactionRepository.existsById(accountDto.getId()))
            throw new Exception("Transaction not found");
        return transactionDtoConverter.toTransactionDTO(transactionRepository.saveAndFlush(transactionDtoConverter.toTransactionEntity(accountDto)));
    }

    @Transactional
    public synchronized void delete(String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        transactionRepository.deleteById(id);
    }

    @Transactional
    public void withdraw(TransactionDto transactionDto) throws Exception {
        Transaction transaction = transactionDtoConverter.toTransactionEntity(transactionDto);
//        accountRepository.getOne(transactionDto.getAccountId());
        Account account = accountRepository.findById(transactionDto.getAccountId()).orElse(null);
        if (account == null)
            throw new Exception("Account not found");
        if (transactionDto.getSum() < 0 &&
                Math.abs(transactionDto.getSum()) > account.getBalance())
            throw new Exception("Insufficient funds in the account");
        account.sumBalance(transactionDto.getSum());

        transaction.setAccount(account);
//        accountRepository.saveAndFlush(account);
        transaction = transactionRepository.saveAndFlush(transaction);
//        accountRepository.sumAccountBalanceById(transactionDto.getAccountId(), transactionDto.getSum());
    }
}
