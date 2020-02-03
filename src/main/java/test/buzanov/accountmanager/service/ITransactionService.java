package test.buzanov.accountmanager.service;

import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.enumurated.TransactionType;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Интерфейс сервиса для сущности Transaction.
 * @author Aleksey Buzanov
 */

public interface ITransactionService {

    Collection<TransactionDto> findAll(int page, int size);

    Collection<TransactionDto> findAllByAccount(final String id, int page, int size) throws Exception;

    Collection<TransactionDto> findAllByCategory(final String id, int page, int size) throws Exception;

    TransactionDto findOne(final String id) throws Exception;

    TransactionDto create(final TransactionDto transactionDto) throws Exception;

    void delete(String id) throws Exception;
}
