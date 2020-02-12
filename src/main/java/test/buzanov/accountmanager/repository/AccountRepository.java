package test.buzanov.accountmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import test.buzanov.accountmanager.entity.Account;
import test.buzanov.accountmanager.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA репозиторий для сущности Account.
 * @author Aleksey Buzanov
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Modifying
    @Transactional
    @Query(value = "update Account a set a.balance = :balance where a.id = :id")
    void setAccountBalanceById(@Param("id") String id, @Param("balance") BigDecimal balance);

    Page<Account> findAllByUsers(Pageable pageable, User user);

    Optional<Account> findAccountByIdAndUsers(String accountId, User user);
}