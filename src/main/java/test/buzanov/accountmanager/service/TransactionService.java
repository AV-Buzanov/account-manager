package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.converter.ITransactionConverter;
import test.buzanov.accountmanager.entity.Transaction;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.form.TransactionForm;
import test.buzanov.accountmanager.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Класс реализует бизнесс логику и валидацию данных для сущности Transaction.
 * @author Aleksey Buzanov
 */

@Service
public class TransactionService implements ITransactionService {

    private static final long LOCK_TIMEOUT = 40000;

    @NotNull
    private Lock lock = new ReentrantLock();

    @NotNull
    private final TransactionRepository transactionRepository;

    @NotNull
    private final ITransactionalActions transactionalActions;

    @NotNull
    private final ITransactionConverter transactionDtoConverter;

    public TransactionService(@NotNull final TransactionRepository transactionRepository,
                              @NotNull final ITransactionalActions transactionalActions,
                              @NotNull final ITransactionConverter transactionDtoConverter) {
        this.transactionRepository = transactionRepository;
        this.transactionalActions = transactionalActions;
        this.transactionDtoConverter = transactionDtoConverter;
    }

    @NotNull
    public Collection<TransactionDto> findAll(int page, int size) {
        return transactionRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(transactionDtoConverter::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @NotNull
    public Collection<TransactionDto> findAllByAccount(@Nullable final String id,
                                                       int page, int size) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        return transactionRepository.findAllByAccountId(id, PageRequest.of(page, size))
                .stream()
                .map(transactionDtoConverter::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @NotNull
    public Collection<TransactionDto> findAllByCategory(@Nullable final String id,
                                                        int page, int size) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        return transactionRepository.findAllByCategoryId(id, PageRequest.of(page, size))
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
    public TransactionDto create(@Nullable final TransactionForm transactionForm, final User user) throws Exception {
        if (transactionForm == null )
            throw new NullPointerException("Id can't be empty or null");
        if (transactionForm.getAccountId() == null || transactionForm.getAccountId().isEmpty())
            throw new NullPointerException("AccountId can't be empty or null");
        if (transactionForm.getCategoryId() == null || transactionForm.getCategoryId().isEmpty())
            throw new NullPointerException("CategoryId can't be empty or null");
        if (transactionForm.getSum() == null || transactionForm.getSum().compareTo(BigDecimal.ZERO) <= 0)
            throw new NullPointerException("Sum can't be null, negative or 0");

        lock.tryLock(LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
        try {
            return transactionDtoConverter.toTransactionDTO(transactionalActions.doTransaction(transactionForm, user));
        } finally {
            lock.unlock();
        }
    }

    public void delete(String id) throws Exception {
        if (id == null || id.isEmpty()) throw new NullPointerException("Id can't by empty or null");
        lock.tryLock(LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
        try {
            transactionalActions.deleteTransaction(id);
        } finally {
            lock.unlock();
        }
    }
}
