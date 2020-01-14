package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.dto.converter.TransactionDtoConverter;
import test.buzanov.accountmanager.entity.Transaction;
import test.buzanov.accountmanager.repository.TransactionRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @NotNull
    @Autowired
    private TransactionRepository transactionRepository;

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
    @Transactional
    public synchronized TransactionDto create(@Nullable final TransactionDto transactionDto) throws Exception {
        if (transactionDto == null || transactionDto.getId() == null || transactionDto.getId().isEmpty()
                || transactionDto.getAccountId() == null || transactionDto.getAccountId().isEmpty() ||
                transactionDto.getSum() == 0)
            throw new Exception("Argument can't be empty or null");
        if (transactionRepository.existsById(transactionDto.getId()))
            throw new Exception("Transaction id already exists");
        Transaction transaction = transactionDtoConverter.toTransactionEntity(transactionDto);
        if (transaction.getAccount() == null)
            throw new Exception("Account not found");
        if (transactionDto.getSum() < 0 &&
                Math.abs(transactionDto.getSum()) > transaction.getAccount().getBalance())
            throw new Exception("Insufficient funds in the account");
        transaction.getAccount().sumBalance(transactionDto.getSum());
        Thread.sleep(5000);
        transaction = transactionRepository.saveAndFlush(transaction);
        return transactionDtoConverter.toTransactionDTO(transaction);
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
}
