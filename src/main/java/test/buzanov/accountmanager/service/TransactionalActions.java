package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.converter.ITransactionConverter;
import test.buzanov.accountmanager.entity.Account;
import test.buzanov.accountmanager.entity.Category;
import test.buzanov.accountmanager.entity.Transaction;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.enumurated.TransactionType;
import test.buzanov.accountmanager.form.TransactionForm;
import test.buzanov.accountmanager.repository.AccountRepository;
import test.buzanov.accountmanager.repository.CategoryRepository;
import test.buzanov.accountmanager.repository.TransactionRepository;

import javax.persistence.EntityNotFoundException;

/**
 * Класс реализует @Transactional методы для сущности Transaction.
 *
 * @author Aleksey Buzanov
 */

@Component
public class TransactionalActions implements ITransactionalActions {
    @NotNull
    private final TransactionRepository transactionRepository;

    @NotNull
    private final AccountRepository accountRepository;

    @NotNull
    private final CategoryRepository categoryRepository;

    @NotNull
    private final ITransactionConverter transactionDtoConverter;

    public TransactionalActions(@NotNull final TransactionRepository transactionRepository,
                                @NotNull final AccountRepository accountRepository,
                                @NotNull final CategoryRepository categoryRepository,
                                @NotNull final ITransactionConverter transactionDtoConverter) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.transactionDtoConverter = transactionDtoConverter;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public Transaction doTransaction(@NotNull final TransactionForm transactionForm, final User user) {
        final Transaction transaction = transactionDtoConverter.toTransactionEntity(transactionForm);
        final Account account = accountRepository.findAccountByIdAndUsers(transactionForm.getAccountId(), user)
                .orElseThrow(() -> new EntityNotFoundException("Account not found."));

        final Category category = categoryRepository.findById(transactionForm.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found."));

        if (category.getTransactionType().equals(TransactionType.WITHDRAW)) {
            if (transactionForm.getSum().compareTo(account.getBalance()) > 0)
                throw new UnsupportedOperationException("Insufficient funds in the account");
            account.subBalance(transactionForm.getSum());
        } else if (category.getTransactionType().equals(TransactionType.DEPOSIT))
            account.addBalance(transactionForm.getSum());
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setTransactionType(category.getTransactionType());
        return transactionRepository.saveAndFlush(transaction);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void deleteTransaction(@NotNull final String id) throws Exception {
        final Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null)
            throw new EntityNotFoundException("Transaction not found");
        final Account account = transaction.getAccount();
        if (account == null)
            throw new EntityNotFoundException("Account not found");
        if (transaction.getTransactionType().equals(TransactionType.WITHDRAW)) {
            if (transaction.getSum().compareTo(account.getBalance()) > 0)
                throw new UnsupportedOperationException("Insufficient funds in the account");
            account.subBalance(transaction.getSum());
        } else if (transaction.getTransactionType().equals(TransactionType.DEPOSIT))
            account.addBalance(transaction.getSum());
        accountRepository.saveAndFlush(account);
        transactionRepository.deleteById(id);
    }
}
