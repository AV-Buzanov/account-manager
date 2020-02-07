package test.buzanov.accountmanager.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import test.buzanov.accountmanager.dto.UserDto;
import test.buzanov.accountmanager.entity.Token;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.repository.TokenRepository;
import test.buzanov.accountmanager.repository.UserRepository;

/**
 * Класс реализует бизнесс логику и валидацию данных для сущности Account.
 * @author Aleksey Buzanov
 */

@Component
public class TokenService implements ITokenService {

    @NotNull
    private final UserRepository userRepository;

    @NotNull
    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    public TokenService(@NotNull UserRepository userRepository, @NotNull TokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Token login(@Nullable UserDto userDto) throws Exception {
        User user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(Exception::new);
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new Exception("Wrong password");
        }
        Token token = new Token();
        token.setValue(RandomStringUtils.random(10, true, true));
        token.setUser(user);

        return tokenRepository.saveAndFlush(token);
    }
}
