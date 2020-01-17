package test.buzanov.accountmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import test.buzanov.accountmanager.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Modifying
    @Query(value = "update Account a set a.balance = :balance where a.id = :id")
    void setAccountBalanceById(@Param("id") String id, @Param("balance") int balance);
}