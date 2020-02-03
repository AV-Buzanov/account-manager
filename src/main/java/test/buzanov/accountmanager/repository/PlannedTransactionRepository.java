package test.buzanov.accountmanager.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import test.buzanov.accountmanager.entity.PlannedTransaction;
import test.buzanov.accountmanager.enumurated.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA репозиторий для сущности PlannedTransaction.
 *
 * @author Aleksey Buzanov
 */

@Repository
public interface PlannedTransactionRepository extends JpaRepository<PlannedTransaction, String> {
    List<PlannedTransaction> findAllByAccountId(String accountId, Pageable pageable);

    List<PlannedTransaction> findAllByCategoryId(String categoryId, Pageable pageable);

    @Query(value = "select sum(t.sum) from PlannedTransaction t where t.account.id=:id and t.transactionType=:type and t.date>:date1 and t.date<:date2")
    Optional<BigDecimal> getSum(@Param("id") String id, @Param("type") TransactionType type, @Param("date1") LocalDate date1, @Param("date2") LocalDate date2);

}