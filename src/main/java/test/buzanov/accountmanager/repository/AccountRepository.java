package test.buzanov.accountmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.buzanov.accountmanager.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

}