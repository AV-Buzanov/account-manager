package test.buzanov.accountmanager.dto.converter;

import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.entity.Transaction;

/**
 * Интерфейс конвертера для сущности Transaction..
 * @author Aleksey Buzanov
 */

public interface ITransactionDtoConverter {
    Transaction toTransactionEntity(final TransactionDto transactionDto);

    TransactionDto toTransactionDTO(final Transaction transaction);
}
