package test.buzanov.accountmanager.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.buzanov.accountmanager.dto.CategoryDto;
import test.buzanov.accountmanager.service.ICategoryService;

import java.util.Collection;

/**
 * Класс публикует REST сервис для управления сущностью Category.
 *
 * @author Aleksey Buzanov
 */

@RestController
@RequestMapping(value = "/category")
public class CategoryRestController {

    private final ICategoryService categoryService;

    public CategoryRestController(@Qualifier(value = "categoryService") final ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<Collection<CategoryDto>> findAll(@RequestHeader("page") final int page,
                                                           @RequestHeader("size") final int size) {
        return ResponseEntity.ok(categoryService.findAll(page, size));
    }

    @GetMapping("/findByParent/{parentId}")
    public ResponseEntity<Collection<CategoryDto>> findAllChilds(@PathVariable final String parentId) {
        return ResponseEntity.ok(categoryService.findAllChilds(parentId));
    }

    @GetMapping("/findAllRoots")
    public ResponseEntity<Collection<CategoryDto>> findAllRoots() {
        return ResponseEntity.ok(categoryService.findAllRoots());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CategoryDto> findOne(@PathVariable final String id) throws Exception {
        final CategoryDto categoryDto = categoryService.findOne(id);
        if (categoryDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(categoryDto);
    }

    @PutMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> create(@RequestBody final CategoryDto categoryDto) throws Exception {
        final CategoryDto createdCategoryDto = categoryService.create(categoryDto);
        if (createdCategoryDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(createdCategoryDto);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> update(@RequestBody final CategoryDto categoryDto) throws Exception {
        final CategoryDto updatedCategoryDto = categoryService.update(categoryDto);
        if (updatedCategoryDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(updatedCategoryDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable final String id) throws Exception {
        categoryService.delete(id);
        return ResponseEntity.ok().build();
    }
}
