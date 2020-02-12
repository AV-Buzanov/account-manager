package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.buzanov.accountmanager.dto.CategoryDto;
import test.buzanov.accountmanager.converter.ICategoryConverter;
import test.buzanov.accountmanager.entity.Category;
import test.buzanov.accountmanager.form.CategoryForm;
import test.buzanov.accountmanager.repository.CategoryRepository;
import test.buzanov.accountmanager.repository.TransactionRepository;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Класс реализует бизнесс логику и валидацию данных для сущности Category.
 *
 * @author Aleksey Buzanov
 */

@Component
public class CategoryService implements ICategoryService {

    @NotNull
    private final CategoryRepository categoryRepository;

    @NotNull
    private final TransactionRepository transactionRepository;

    @NotNull
    private final ICategoryConverter categoryDtoConverter;

    public CategoryService(@NotNull final CategoryRepository categoryRepository,
                           @NotNull final TransactionRepository transactionRepository,
                           @NotNull final ICategoryConverter categoryDtoConverter) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.categoryDtoConverter = categoryDtoConverter;
    }

    public Collection<CategoryDto> findAll(final int page, final int size) {
        return categoryRepository.findAll(PageRequest.of(page, size)).stream()
                .map(categoryDtoConverter::toCategoryDTO)
                .collect(Collectors.toList());
    }

    public Collection<CategoryDto> findAllChilds(final String parentId) {
        return categoryRepository.findAllByParentId(parentId).stream()
                .map(categoryDtoConverter::toCategoryDTO)
                .collect(Collectors.toList());
    }

    public Collection<CategoryDto> findAllRoots() {
        return categoryRepository.findAllByParentId(null).stream()
                .map(categoryDtoConverter::toCategoryDTO)
                .collect(Collectors.toList());
    }

    @Nullable
    public CategoryDto findOne(@Nullable final String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        @Nullable final CategoryDto categoryDto = categoryDtoConverter
                .toCategoryDTO(categoryRepository.findById(id).orElse(null));
        return categoryDto;
    }

    @Nullable
    @Transactional
    public CategoryDto create(@Nullable final CategoryForm categoryForm) throws Exception {
        if (categoryForm == null)
            throw new Exception("Argument can't be empty or null");
        final Category account = categoryDtoConverter.toCategoryEntity(categoryForm);
        if (categoryForm.getParentId() != null)
            account.setParent(categoryRepository
                    .findById(categoryForm.getParentId()).orElse(null));
        return categoryDtoConverter.toCategoryDTO(categoryRepository.saveAndFlush(account));
    }

    @Nullable
    @Transactional
    public CategoryDto update(@Nullable final CategoryForm categoryForm, String id) throws Exception {
        if (categoryForm == null)
            throw new Exception("Argument can't be empty or null");
        final Category category = categoryRepository.findById(id).orElse(null);
        if (category == null)
            throw new Exception("Category not found");
        final Category convertedCategoryForUpdate = categoryDtoConverter.toCategoryEntity(categoryForm);
        if (categoryForm.getParentId() != null)
            convertedCategoryForUpdate.setParent(categoryRepository.findById(categoryForm.getParentId()).orElse(null));
        return categoryDtoConverter.toCategoryDTO(categoryRepository.saveAndFlush(convertedCategoryForUpdate));
    }

    @Transactional
    public void delete(String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        categoryRepository.deleteById(id);
    }
}
