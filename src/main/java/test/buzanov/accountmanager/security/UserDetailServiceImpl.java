package test.buzanov.accountmanager.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.service.IUserService;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final IUserService userService;

    public UserDetailServiceImpl(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        final User user = userService.findByUsername(s);
        if (user == null)
            throw new UsernameNotFoundException(s);
        return user;
    }
}
