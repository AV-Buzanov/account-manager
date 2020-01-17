package test.buzanov.accountmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import test.buzanov.accountmanager.entity.Transaction;

import java.math.BigDecimal;
import java.util.Collection;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Collection<Transaction> findAllByAccountId(String id);

    @Query(value = "select sum(t.sum) from Transaction t where t.account.id=:id and t.transactionType='DEPOSIT'")
    BigDecimal getDepositOperationsSum(@Param("id") String id);

    @Query(value = "select sum(t.sum) from Transaction t where t.account.id=:id and t.transactionType='WITHDRAW'")
    BigDecimal getWithdrawOperationsSum(@Param("id") String id);
}