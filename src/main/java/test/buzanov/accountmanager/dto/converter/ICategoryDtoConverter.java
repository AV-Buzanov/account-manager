package test.buzanov.accountmanager.dto.converter;

import test.buzanov.accountmanager.dto.CategoryDto;
import test.buzanov.accountmanager.entity.Category;

/**
 * Интерфейс конвертера для сущности Category.
 *
 * @author Aleksey Buzanov
 */

public interface ICategoryDtoConverter {
    Category toCategoryEntity(final CategoryDto categorytDto);

    CategoryDto toCategoryDTO(final Category category);
}
