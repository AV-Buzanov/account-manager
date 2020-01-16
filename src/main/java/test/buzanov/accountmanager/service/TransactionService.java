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
        lock.tryLock(40000, TimeUnit.MILLISECONDS);
        try {
            return transactionDtoConverter.toTransactionDTO(doTransaction(transactionDto));
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public Transaction doTransaction(@NotNull final TransactionDto transactionDto) throws Exception {
        final Transaction transaction = transactionDtoConverter.toTransactionEntity(transactionDto);
        if (transactionDto.getAccountId() == null || transaction == null)
            throw new Exception("Null");
        final Account account = accountRepository.findById(transactionDto.getAccountId()).orElse(null);
        if (account == null)
            throw new Exception("Account not found");
        if (transactionDto.getSum() < 0 &&
                Math.abs(transactionDto.getSum()) > account.getBalance())
            throw new Exception("Insufficient funds in the account");
        account.sumBalance(transactionDto.getSum());
        transaction.setAccount(account);
        return transactionRepository.saveAndFlush(transaction);
    }

    @Nullable
    public TransactionDto update(@Nullable final TransactionDto transactionDto) throws Exception {
        if (transactionDto == null || transactionDto.getId() == null || transactionDto.getId().isEmpty())
            throw new Exception("Id can't be empty or null");
        if (transactionDto.getAccountId() == null || transactionDto.getAccountId().isEmpty())
            throw new Exception("AccountId can't be empty or null");
        lock.tryLock(40000, TimeUnit.MILLISECONDS);
        try {
            return transactionDtoConverter.toTransactionDTO(updateTransaction(transactionDto));
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public Transaction updateTransaction(@NotNull final TransactionDto transactionDto) throws Exception {
        final Transaction transaction = transactionRepository.findById(transactionDto.getId()).orElse(null);
        if (transaction == null)
            throw new Exception("Transaction not found");
        final Transaction convertedNewTransaction = transactionDtoConverter.toTransactionEntity(transactionDto);
        final Account account = accountRepository.findById(transactionDto.getAccountId()).orElse(null);
        if (account == null)
            throw new Exception("Account not found");
        if (transactionDto.getSum() != 0) {
            int withdrawSum = transactionDto.getSum() - transaction.getSum();
            if (transactionDto.getSum() < 0 &&
                    Math.abs(withdrawSum) > account.getBalance())
                throw new Exception("Insufficient funds in the account");
            account.sumBalance(withdrawSum);
        } else {
            convertedNewTransaction.setSum(transaction.getSum());
        }
        convertedNewTransaction.setAccount(account);
        return transactionRepository.saveAndFlush(convertedNewTransaction);
    }

    @Transactional
    public void delete(String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        final Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null)
            throw new Exception("Transaction not found");
        lock.tryLock(40000, TimeUnit.MILLISECONDS);
        try {
            transactionRepository.deleteById(id);
        } finally {
            lock.unlock();
        }
    }

}
