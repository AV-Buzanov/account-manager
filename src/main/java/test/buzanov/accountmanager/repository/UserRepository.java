package test.buzanov.accountmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import test.buzanov.accountmanager.entity.User;

import java.util.Optional;

/**
 * Spring Data JPA репозиторий для сущности User.
 * @author Aleksey Buzanov
 */

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    @Transactional
    void deleteUserByUsername(String username);

    boolean existsUserByUsername(String username);
}