package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @NotNull
    @Autowired
    private TransactionDtoConverter transactionDtoConverter;

    @NotNull
    private Lock lock = new ReentrantLock();

    @NotNull
    public Collection<TransactionDto> findAll() {
        return transactionRepository.findAll().stream().map(transactionDtoConverter::toTransactionDTO).collect(Collectors.toList());
    }

    @NotNull
    public Collection<TransactionDto> findAllByAccount(@Nullable final String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        return transactionRepository.findAllByAccountId(id).stream().map(transactionDtoConverter::toTransactionDTO).collect(Collectors.toList());
    }

    @Nullable
    public TransactionDto findOne(@Nullable final String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        return transactionDtoConverter.toTransactionDTO(transactionRepository.findById(id).orElseThrow(null));
    }

    @Nullable
    public TransactionDto create(@Nullable final TransactionDto transactionDto) throws Exception {
        if (transactionDto == null || transactionDto.getId() == null || transactionDto.getId().isEmpty())
            throw new Exception("Id can't be empty or null");
        if (transactionDto.getAccountId() == null || transactionDto.getAccountId().isEmpty())
            throw new Exception("AccountId can't be empty or null");
        if (transactionDto.getSum() == 0)
            throw new Exception("Sum can't be 0");
        if (transactionRepository.existsById(transactionDto.getId()))
            throw new Exception("Transaction id already exists");
        final Transaction transaction = transactionDtoConverter.toTransactionEntity(transactionDto);
        lock.tryLock(40000, TimeUnit.MILLISECONDS);
        try {
            Account account = doTransaction(transactionDto.getAccountId(), transactionDto.getSum());
            transaction.setAccount(account);
            transactionRepository.saveAndFlush(transaction);
        } finally {
            lock.unlock();
        }
        return transactionDtoConverter.toTransactionDTO(transactionRepository.findById(transactionDto.getId()).orElse(null));
    }

    @Nullable
    @Transactional
    public TransactionDto update(@Nullable final TransactionDto transactionDto) throws Exception {
        if (transactionDto == null || transactionDto.getId() == null || transactionDto.getId().isEmpty())
            throw new Exception("Id can't be empty or null");
        if (transactionDto.getAccountId() == null || transactionDto.getAccountId().isEmpty())
            throw new Exception("AccountId can't be empty or null");
        final Transaction transaction = transactionRepository.findById(transactionDto.getId()).orElse(null);
        final Transaction convertedNewTransaction = transactionDtoConverter.toTransactionEntity(transactionDto);
        if (transaction == null || convertedNewTransaction == null)
            throw new Exception("Transaction not found");
        if (transactionDto.getSum() != 0) {
            lock.tryLock(40000, TimeUnit.MILLISECONDS);
            try {
                doTransaction(transactionDto.getAccountId(), transactionDto.getSum() - transaction.getSum());
                convertedNewTransaction.setAccount(transaction.getAccount());
                transactionRepository.saveAndFlush(convertedNewTransaction);
            } finally {
                lock.unlock();
            }
        } else {
            convertedNewTransaction.setSum(transaction.getSum());
            transactionRepository.saveAndFlush(convertedNewTransaction);
        }
        return transactionDtoConverter.toTransactionDTO(transactionRepository.findById(transactionDto.getId()).orElse(null));
    }

    @Transactional
    public void delete(String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        final Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null)
            throw new Exception("Transaction not found");
        lock.tryLock(40000, TimeUnit.MILLISECONDS);
        try {
            doTransaction(transaction.getAccount().getId(), -transaction.getSum());
            transactionRepository.deleteById(id);
        } finally {
            lock.unlock();
        }
    }
    @Transactional
    public Account doTransaction(@NotNull final String accountId, int sum) throws Exception {
        final Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null)
            throw new Exception("Account not found");
        if (sum < 0 &&
                Math.abs(sum) > account.getBalance())
            throw new Exception("Insufficient funds in the account");
        account.sumBalance(sum);
        return accountRepository.saveAndFlush(account);
//        accountRepository.sumAccountBalanceById(accountId, sum);
    }
}
