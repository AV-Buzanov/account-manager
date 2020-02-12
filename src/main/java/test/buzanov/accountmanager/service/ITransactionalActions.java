package test.buzanov.accountmanager.service;

import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.entity.Transaction;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.form.TransactionForm;

/**
 * Интерфейс транзакционных методов сущности Transaction.
 * @author Aleksey Buzanov
 */

public interface ITransactionalActions {

    Transaction doTransaction(final TransactionForm transactionForm, final User user) ;

    void deleteTransaction(final String id) throws Exception;
}
