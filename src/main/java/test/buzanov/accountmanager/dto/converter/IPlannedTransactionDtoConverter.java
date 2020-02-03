package test.buzanov.accountmanager.dto.converter;

import test.buzanov.accountmanager.dto.PlannedTransactionDto;
import test.buzanov.accountmanager.entity.PlannedTransaction;

/**
 * Интерфейс конвертера для сущности PlannedTransaction..
 *
 * @author Aleksey Buzanov
 */

public interface IPlannedTransactionDtoConverter {
    PlannedTransaction toTransactionEntity(final PlannedTransactionDto transactionDto);

    PlannedTransactionDto toTransactionDTO(final PlannedTransaction transaction);
}
