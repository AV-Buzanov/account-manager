package test.buzanov.accountmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.buzanov.accountmanager.entity.Category;

import java.util.Collection;

/**
 * Spring Data JPA репозиторий для сущности Account.
 *
 * @author Aleksey Buzanov
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Collection<Category> findAllByParentId(String parentId);
}