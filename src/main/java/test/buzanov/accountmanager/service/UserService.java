package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.buzanov.accountmanager.entity.Category;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.repository.UserRepository;

import java.util.Collection;

/**
 * Класс реализует бизнесс логику и валидацию данных для сущности Account.
 *
 * @author Aleksey Buzanov
 */

@Component
public class UserService implements IUserService {

    @NotNull
    private final UserRepository userRepository;

    public UserService(@NotNull UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Collection<User> findAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Nullable
    public User findOne(@Nullable final String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(@Nullable String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Nullable
    @Transactional
    public User create(@Nullable final User user) throws Exception {
        if (user == null || user.getId() == null || user.getId().isEmpty()
                || user.getUsername() == null || user.getUsername().isEmpty()
                || user.getPassword() == null || user.getPassword().isEmpty()
                || user.getName() == null || user.getName().isEmpty())
            throw new Exception("Argument can't be empty or null");
        if (userRepository.existsUserByUsername(user.getUsername()))
            throw new Exception("This username already exists.");
        return userRepository.saveAndFlush(user);
    }

    @Nullable
    @Transactional
    public User update(@Nullable final User user) throws Exception {
        if (user == null || user.getId() == null || user.getId().isEmpty())
            throw new Exception("Argument can't be empty or null");
        final User ourUser = userRepository.findById(user.getId()).orElse(null);
        if (ourUser == null)
            throw new Exception("User not found.");
        if (!user.getUsername().equals(ourUser.getUsername())
                && userRepository.existsUserByUsername(user.getUsername()))
            throw new Exception("This username already exists.");
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public void delete(String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        userRepository.deleteById(id);
    }
}
