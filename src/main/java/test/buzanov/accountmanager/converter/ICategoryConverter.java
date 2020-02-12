package test.buzanov.accountmanager.converter;

import test.buzanov.accountmanager.dto.CategoryDto;
import test.buzanov.accountmanager.entity.Category;
import test.buzanov.accountmanager.form.CategoryForm;

/**
 * Интерфейс конвертера для сущности Category.
 *
 * @author Aleksey Buzanov
 */

public interface ICategoryConverter {
    Category toCategoryEntity(final CategoryForm categoryForm);

    CategoryDto toCategoryDTO(final Category category);
}
