package test.buzanov.accountmanager.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import test.buzanov.accountmanager.dto.CategoryDto;
import test.buzanov.accountmanager.entity.Category;
import test.buzanov.accountmanager.form.CategoryForm;

/**
 * Класс реализует перевод сущности Category в DTO объект и обратно.
 *
 * @author Aleksey Buzanov
 */

@Component
public class CategoryConverter implements ICategoryConverter {
    @Nullable
    public Category toCategoryEntity(@Nullable final CategoryForm categoryForm) {
        if (categoryForm == null) return null;
        @NotNull final Category category = new Category();
        category.setName(categoryForm.getName());
        category.setDescription(categoryForm.getDescription());
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
