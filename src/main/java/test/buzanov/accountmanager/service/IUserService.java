package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.entity.User;

import java.util.Collection;

/**
 * Интерфейс сервиса для сущности Transaction.
 * @author Aleksey Buzanov
 */

public interface IUserService {
    Collection<User> findAll(int page, int size);

    User findOne(@Nullable final String id) throws Exception;

    User findByUsername(@Nullable final String username);

    User create(@Nullable final User accountDto) throws Exception;

    User update(@Nullable final User accountDto) throws Exception;

    void delete(String id) throws Exception;
}
