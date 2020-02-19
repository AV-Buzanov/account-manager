package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.enumurated.TransactionType;
import test.buzanov.accountmanager.form.TransactionForm;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Интерфейс сервиса для сущности Transaction.
 * @author Aleksey Buzanov
 */

public interface ITransactionService {

    Collection<TransactionDto> findAll(int page, int size);

    Collection<TransactionDto> findAllByAccount(final String id, int page, int size) throws Exception;

    Collection<TransactionDto> findAllByCategory(final String id, int page, int size) throws Exception;

    TransactionDto findOne(final String id) throws Exception;

    TransactionDto create(final TransactionForm transactionDto, final User user) throws Exception;

    Collection<TransactionDto> findAllByAccountAndCategory(@Nullable final String accountId,
                                                           @Nullable final String categoryId,
                                                           int page, int size) throws Exception;

    List<TransactionDto> update(@Nullable final User user, final Date date);

    void delete(String id) throws Exception;
}
