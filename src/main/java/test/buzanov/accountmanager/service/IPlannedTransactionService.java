package test.buzanov.accountmanager.service;

import test.buzanov.accountmanager.dto.PlannedTransactionDto;

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

    void delete(String id) throws Exception;
}
