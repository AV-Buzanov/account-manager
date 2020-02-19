package test.buzanov.accountmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.buzanov.accountmanager.entity.Account;
import test.buzanov.accountmanager.entity.Category;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.enumurated.TransactionType;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA репозиторий для сущности Category.
 *
 * @author Aleksey Buzanov
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    Collection<Category> findAllByParentId(String parentId);

    Collection<Category> findAllByTransactionType(TransactionType transactionType);

    Optional<List<Category>> findAllByAccountUsersAndUpdateIsAfter(User user, Date date);
}