package test.buzanov.accountmanager.dto.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import test.buzanov.accountmanager.dto.CategoryDto;
import test.buzanov.accountmanager.entity.Category;

/**
 * Класс реализует перевод сущности Category в DTO объект и обратно.
 *
 * @author Aleksey Buzanov
 */

@Component
public class CategoryDtoConverter implements ICategoryDtoConverter {
    @Nullable
    public Category toCategoryEntity(@Nullable final CategoryDto categoryDto) {
        if (categoryDto == null || categoryDto.getId() == null) return null;
        @NotNull final Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setTransactionType(categoryDto.getTransactionType());
        return category;
    }

    @Nullable
    public CategoryDto toCategoryDTO(@Nullable final Category category) {
        if (category == null) return null;
        @NotNull final CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        if (category.getParent() != null)
            categoryDto.setParentId(category.getParent().getId());
        categoryDto.setTransactionType(category.getTransactionType());
        return categoryDto;
    }
}
