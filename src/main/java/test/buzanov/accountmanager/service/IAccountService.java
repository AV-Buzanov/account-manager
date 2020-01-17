package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.dto.AccountDto;

import java.util.Collection;

public interface IAccountService {
    Collection<AccountDto> findAll();
    
    AccountDto findOne(@Nullable final String id) throws Exception;
    
    AccountDto create(@Nullable final AccountDto accountDto) throws Exception;
    
    AccountDto update(@Nullable final AccountDto accountDto) throws Exception;
    
    void delete(String id) throws Exception;
}
