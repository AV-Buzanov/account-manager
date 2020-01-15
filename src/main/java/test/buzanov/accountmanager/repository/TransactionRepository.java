package test.buzanov.accountmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import test.buzanov.accountmanager.entity.Transaction;

import java.util.Collection;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Collection<Transaction> findAllByAccountId(String id);
}