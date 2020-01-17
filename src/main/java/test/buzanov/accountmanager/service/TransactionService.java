package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.dto.converter.TransactionDtoConverter;
import test.buzanov.accountmanager.entity.Account;
import test.buzanov.accountmanager.entity.Transaction;
import test.buzanov.accountmanager.enumurated.TransactionType;
import test.buzanov.accountmanager.repository.AccountRepository;
import test.buzanov.accountmanager.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final long LOCK_TIMEOUT = 40000;

    @NotNull
    private Lock lock = new ReentrantLock();

    @NotNull
    private final TransactionRepository transactionRepository;

    @NotNull
    private final AccountRepository accountRepository;

    @NotNull
    private final TransactionDtoConverter transactionDtoConverter;

    public TransactionService(@NotNull final TransactionRepository transactionRepository,
                              @NotNull final AccountRepository accountRepository,
                              @NotNull final TransactionDtoConverter transactionDtoConverter) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionDtoConverter = transactionDtoConverter;
    }

    @NotNull
    public Collection<TransactionDto> findAll() {
        return transactionRepository.findAll()
                .stream()
                .map(transactionDtoConverter::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @NotNull
    public Collection<TransactionDto> findAllByAccount(@Nullable final String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        return transactionRepository.findAllByAccountId(id)
                .stream()
                .map(transactionDtoConverter::toTransactionDTO)
                .collect(Collectors.toList());
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
        if (transactionDto.getSum().compareTo(BigDecimal.valueOf(0)) <= 0)
            throw new Exception("Sum can't be negative or 0");
        if (transactionRepository.existsById(transactionDto.getId()))
            throw new Exception("Transaction id already exists");
        lock.tryLock(LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
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
        if (transactionDto.getTransactionType().equals(TransactionType.WITHDRAW)) {
            if (transactionDto.getSum().compareTo(account.getBalance()) > 0)
                throw new Exception("Insufficient funds in the account");
            account.subBalance(transactionDto.getSum());
        } else if (transactionDto.getTransactionType().equals(TransactionType.DEPOSIT))
            account.addBalance(transactionDto.getSum());
        transaction.setAccount(account);
        return transactionRepository.saveAndFlush(transaction);
    }

    @Nullable
    public TransactionDto update(@Nullable final TransactionDto transactionDto) throws Exception {
        if (transactionDto == null || transactionDto.getId() == null || transactionDto.getId().isEmpty())
            throw new Exception("Id can't be empty or null");
        if (transactionDto.getAccountId() == null || transactionDto.getAccountId().isEmpty())
            throw new Exception("AccountId can't be empty or null");
        lock.tryLock(LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
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
        if (transactionDto.getSum().compareTo(BigDecimal.valueOf(0)) != 0) {
            final BigDecimal withdrawSum = transactionDto.getSum().subtract(transaction.getSum());
            if (withdrawSum.intValue() < 0 &&
                    Math.abs(withdrawSum.intValue()) > account.getBalance().intValue())
                throw new Exception("Insufficient funds in the account");
            account.addBalance(withdrawSum);
        } else {
            convertedNewTransaction.setSum(transaction.getSum());
        }
        convertedNewTransaction.setAccount(account);
        return transactionRepository.saveAndFlush(convertedNewTransaction);
    }

    public void delete(String id) throws Exception {
        if (id == null || id.isEmpty()) throw new NullPointerException("Id can't by empty or null");
        lock.tryLock(LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
        try {
            deleteTransaction(id);
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public void deleteTransaction(@NotNull final String id) throws Exception {
        final Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null)
            throw new Exception("Transaction not found");
        final Account account = transaction.getAccount();
        if (account == null)
            throw new Exception("Account not found");
        BigDecimal withdrawSum = transaction.getSum().negate();
        if (withdrawSum.intValue() < 0 &&
                Math.abs(withdrawSum.intValue()) > account.getBalance().intValue())
            throw new Exception("Insufficient funds in the account");
        account.addBalance(withdrawSum);
        accountRepository.saveAndFlush(account);
        transactionRepository.deleteById(id);
    }
}
