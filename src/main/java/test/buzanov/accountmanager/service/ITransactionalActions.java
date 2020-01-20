package test.buzanov.accountmanager.service;

import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.entity.Transaction;

public interface ITransactionalActions {

    Transaction doTransaction(final TransactionDto transactionDto) throws Exception;

    void deleteTransaction(final String id) throws Exception;
}
