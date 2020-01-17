package test.buzanov.accountmanager.service;

import test.buzanov.accountmanager.dto.TransactionDto;

import java.util.Collection;

public interface ITransactionService {
  
    Collection<TransactionDto> findAll();
    
    Collection<TransactionDto> findAllByAccount(final String id) throws Exception;
    
    TransactionDto findOne(final String id) throws Exception;

    TransactionDto create(final TransactionDto transactionDto) throws Exception;
    
    void delete(String id) throws Exception;
}
