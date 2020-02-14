package test.buzanov.accountmanager.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.buzanov.accountmanager.converter.IUserConverter;
import test.buzanov.accountmanager.security.JWTAuthenticationFilter;
import test.buzanov.accountmanager.security.JWTAuthorizationFilter;
import test.buzanov.accountmanager.security.UserDetailServiceImpl;


/**
 * Класс конфигурации Spring Security.
 *
 * @author Aleksey Buzanov
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] SWAGGER_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
    };

    private final PasswordEncoder passwordEncoder;

    private final UserDetailServiceImpl userDetailService;

    private final IUserConverter userConverter;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expireTime}")
    private String expireTime;


    public SecurityConfig(PasswordEncoder passwordEncoder, UserDetailServiceImpl userDetailService, IUserConverter userConverter) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailService = userDetailService;
        this.userConverter = userConverter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/auth/registration", "/login", "/*").permitAll()
                .antMatchers(SWAGGER_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(JWTAuthenticationFilter.builder()
                        .authenticationManager(authenticationManager())
                        .secret(secret)
                        .expiredTimeMillis(Long.parseLong(expireTime))
                        .userConverter(userConverter).build())
                .addFilter(JWTAuthorizationFilter.builder()
                        .secret(secret)
                        .authenticationManager(authenticationManager()).build())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
    }
}
