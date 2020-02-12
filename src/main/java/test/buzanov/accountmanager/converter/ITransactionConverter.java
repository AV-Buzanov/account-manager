package test.buzanov.accountmanager.converter;

import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.entity.Transaction;
import test.buzanov.accountmanager.form.TransactionForm;

/**
 * Интерфейс конвертера для сущности Transaction..
 * @author Aleksey Buzanov
 */

public interface ITransactionConverter {
    Transaction toTransactionEntity(final TransactionForm transactionForm);

    TransactionDto toTransactionDTO(final Transaction transaction);
}
