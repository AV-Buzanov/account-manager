package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.dto.converter.ITransactionDtoConverter;
import test.buzanov.accountmanager.entity.Account;
import test.buzanov.accountmanager.entity.Transaction;
import test.buzanov.accountmanager.enumurated.TransactionType;
import test.buzanov.accountmanager.repository.AccountRepository;
import test.buzanov.accountmanager.repository.TransactionRepository;

/**
 * Класс реализует @Transactional методы для сущности Transaction.
 * @author Aleksey Buzanov
 */

@Component
public class TransactionalActions implements ITransactionalActions {
    @NotNull
    private final TransactionRepository transactionRepository;

    @NotNull
    private final AccountRepository accountRepository;

    @NotNull
    private final ITransactionDtoConverter transactionDtoConverter;

    public TransactionalActions(@NotNull final TransactionRepository transactionRepository,
                                @NotNull final AccountRepository accountRepository,
                                @NotNull final ITransactionDtoConverter transactionDtoConverter) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionDtoConverter = transactionDtoConverter;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
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

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void deleteTransaction(@NotNull final String id) throws Exception {
        final Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null)
            throw new Exception("Transaction not found");
        final Account account = transaction.getAccount();
        if (account == null)
            throw new Exception("Account not found");
        if (transaction.getTransactionType().equals(TransactionType.WITHDRAW)) {
            if (transaction.getSum().compareTo(account.getBalance()) > 0)
                throw new Exception("Insufficient funds in the account");
            account.subBalance(transaction.getSum());
        } else if (transaction.getTransactionType().equals(TransactionType.DEPOSIT))
            account.addBalance(transaction.getSum());
        accountRepository.saveAndFlush(account);
        transactionRepository.deleteById(id);
    }
}
