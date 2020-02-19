package test.buzanov.accountmanager.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import test.buzanov.accountmanager.entity.Category;
import test.buzanov.accountmanager.entity.Transaction;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.enumurated.TransactionType;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA репозиторий для сущности Transaction.
 * @author Aleksey Buzanov
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findAllByAccountId(String accountId, Pageable pageable);

    List<Transaction> findAllByCategoryId(String categoryId, Pageable pageable);

    List<Transaction> findAllByAccountIdAndCategoryId(String accountId, String categoryId, Pageable pageable);

    List<Transaction> findAllByAccountIdAndTransactionType(String categoryId, TransactionType type, Pageable pageable);


    @Query(value = "from Transaction t where t.account.id=:id and t.transactionType='DEPOSIT'")
    List<Transaction> findAllByCategoryIdCase(String categoryId, String accountId, Pageable pageable);

    @Query(value = "select sum(t.sum) from Transaction t where t.account.id=:id and t.transactionType='DEPOSIT'")
    BigDecimal getDepositOperationsSum(@Param("id") String id);

    @Query(value = "select sum(t.sum) from Transaction t where t.account.id=:id and t.transactionType='WITHDRAW'")
    BigDecimal getWithdrawOperationsSum(@Param("id") String id);

    Optional<List<Transaction>> findAllByAccountUsersAndUpdateAfter(User user, Date date);

    @Query(value = "select t.account.id from Transaction t where t.id=:id")
    Optional<String> getAccountId(@Param("id") String id);
}