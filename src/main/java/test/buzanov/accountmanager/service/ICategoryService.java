package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.dto.CategoryDto;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.form.CategoryForm;

import java.util.Collection;

/**
 * Интерфейс сервиса для сущности Category.
 *
 * @author Aleksey Buzanov
 */

public interface ICategoryService {
    Collection<CategoryDto> findAll(int page, int size);

    Collection<CategoryDto> findAllChilds(String parentId);

    Collection<CategoryDto> findAllRoots();

    CategoryDto findOne(@Nullable final String id) throws Exception;

    CategoryDto create(@Nullable final CategoryForm accountDto, final User user) throws Exception;

    CategoryDto update(@Nullable final CategoryForm accountDto, String id) throws Exception;

    void delete(String id) throws Exception;
}
