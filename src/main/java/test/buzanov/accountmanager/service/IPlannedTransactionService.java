package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.dto.PlannedTransactionDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

/**
 * Интерфейс сервиса для сущности PlannedTransaction.
 *
 * @author Aleksey Buzanov
 */

public interface IPlannedTransactionService {

    Collection<PlannedTransactionDto> findAll(int page, int size);

    Collection<PlannedTransactionDto> findAllByAccount(final String id, int page, int size) throws Exception;

    Collection<PlannedTransactionDto> findAllByCategory(final String id, int page, int size) throws Exception;

    PlannedTransactionDto findOne(final String id) throws Exception;

    PlannedTransactionDto create(final PlannedTransactionDto transactionDto) throws Exception;

    BigDecimal getPlannedSumOnDate(@Nullable final String id, LocalDate localDate);

    void delete(String id) throws Exception;
}
