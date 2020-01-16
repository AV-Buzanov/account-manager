package test.buzanov.accountmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import test.buzanov.accountmanager.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Modifying
    @Transactional
    @Query(value = "update Account a set a.balance = a.balance+:sum where a.id = :id")
    void sumAccountBalanceById(@Param("id")String id, @Param("sum") int sum);
}