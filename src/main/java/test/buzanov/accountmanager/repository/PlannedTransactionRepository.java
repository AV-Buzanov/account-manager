package test.buzanov.accountmanager.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import test.buzanov.accountmanager.entity.PlannedTransaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data JPA репозиторий для сущности PlannedTransaction.
 *
 * @author Aleksey Buzanov
 */

@Repository
public interface PlannedTransactionRepository extends JpaRepository<PlannedTransaction, String> {
    List<PlannedTransaction> findAllByAccountId(String accountId, Pageable pageable);

    List<PlannedTransaction> findAllByCategoryId(String categoryId, Pageable pageable);

    @Query(value = "select sum(t.sum) from PlannedTransaction t where t.account.id=:id and t.transactionType='DEPOSIT'")
    BigDecimal getDepositOperationsSum(@Param("id") String id);

    @Query(value = "select sum(t.sum) from PlannedTransaction t where t.account.id=:id and t.transactionType='WITHDRAW'")
    BigDecimal getWithdrawOperationsSum(@Param("id") String id);


}