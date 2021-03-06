package test.buzanov.accountmanager.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import test.buzanov.accountmanager.dto.CategoryDto;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.form.CategoryForm;
import test.buzanov.accountmanager.service.ICategoryService;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Класс публикует REST сервис для управления сущностью Category.
 *
 * @author Aleksey Buzanov
 */

@RestController
@RequestMapping(value = "/categories")
public class CategoryRestController {

    private final ICategoryService categoryService;

    public CategoryRestController(@Qualifier(value = "categoryService") final ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<Collection<CategoryDto>> findAll(@RequestHeader(value = "page", defaultValue = "0") final int page,
                                                           @RequestHeader(value = "size", defaultValue = "100") final int size) {
        return ResponseEntity.ok(categoryService.findAll(page, size));
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<Collection<CategoryDto>> findAllChilds(@PathVariable final String parentId) {
        return ResponseEntity.ok(categoryService.findAllChilds(parentId));
    }

    @GetMapping("/roots")
    public ResponseEntity<Collection<CategoryDto>> findAllRoots() {
        return ResponseEntity.ok(categoryService.findAllRoots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findOne(@PathVariable final String id) throws Exception {
        final CategoryDto categoryDto = categoryService.findOne(id);
        if (categoryDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(categoryDto);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> create(@RequestBody final CategoryForm categoryDto,
                                              @ApiIgnore @AuthenticationPrincipal User user) throws Exception {
        final CategoryDto createdCategoryDto = categoryService.create(categoryDto, user);
        if (createdCategoryDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(createdCategoryDto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> update(@RequestBody final CategoryForm categoryDto,
                                              @PathVariable final String id) throws Exception {
        final CategoryDto updatedCategoryDto = categoryService.update(categoryDto, id);
        if (updatedCategoryDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(updatedCategoryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable final String id) throws Exception {
        categoryService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/update/{timestamp}")
    public ResponseEntity<List<CategoryDto>> update(@PathVariable("timestamp") final Long timestamp,
                                                       @ApiIgnore @AuthenticationPrincipal final User user) {
        return ResponseEntity.ok(categoryService.update(user, new Date(timestamp)));
    }
}
