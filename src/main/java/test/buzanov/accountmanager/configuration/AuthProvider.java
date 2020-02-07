package test.buzanov.accountmanager.configuration;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.enumurated.Role;
import test.buzanov.accountmanager.service.IUserService;

import java.util.Collection;
import java.util.HashSet;

@Component
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService userService;

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = ((String) authentication.getCredentials());
//        Collection<Role> authorities = new HashSet<>();
//        authorities.add(Role.USER);
//        if (username.equals("user")&&password.equals("user"))
//            return new UsernamePasswordAuthenticationToken(username, password,authorities );


        User user = userService.findByUsername(username);
        if(user != null && (user.getUsername().equals(username) || user.getName().equals(username))) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Wrong password");
            }
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            return new UsernamePasswordAuthenticationToken(user, password, authorities);
        }
        else
            throw new BadCredentialsException("Username not found");

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
