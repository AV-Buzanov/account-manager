package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.dto.CategoryDto;

import java.util.Collection;

/**
 * Интерфейс сервиса для сущности Category.
 *
 * @author Aleksey Buzanov
 */

public interface ICategoryService {
    Collection<CategoryDto> findAll(int page, int size);

    Collection<CategoryDto> findAllChilds(String parentId);

    CategoryDto findOne(@Nullable final String id) throws Exception;

    CategoryDto create(@Nullable final CategoryDto accountDto) throws Exception;

    CategoryDto update(@Nullable final CategoryDto accountDto) throws Exception;

    void delete(String id) throws Exception;
}
