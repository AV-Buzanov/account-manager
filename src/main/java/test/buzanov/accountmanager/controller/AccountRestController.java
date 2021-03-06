package test.buzanov.accountmanager.controller;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.form.AccountForm;
import test.buzanov.accountmanager.service.IAccountService;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Класс публикует REST сервис для управления сущностью Account.
 *
 * @author Aleksey Buzanov
 */

@RestController
@RequestMapping(value = "/accounts")
public class AccountRestController {

    private final IAccountService accountService;

    public AccountRestController(@Qualifier(value = "accountService") final IAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public ResponseEntity<Collection<AccountDto>> findAll(@RequestHeader(value = "page", defaultValue = "0") final int page,
                                                          @RequestHeader(value = "size", defaultValue = "100") final int size,
                                                          @ApiIgnore @AuthenticationPrincipal final User user) {
        return ResponseEntity.ok(accountService.findAll(page, size, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> findOne(@PathVariable(required = false) final String id,
                                              @ApiIgnore @AuthenticationPrincipal final User user) {
        final AccountDto accountDto = accountService.findOne(id, user);
        if (accountDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping("/update/{timestamp}")
    public ResponseEntity<List<AccountDto>> update(@PathVariable("timestamp") final Long timestamp,
                                                    @ApiIgnore @AuthenticationPrincipal final User user) {
        return ResponseEntity.ok(accountService.update(user, new Date(timestamp)));
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> create(@RequestBody final AccountForm accountForm,
                                             @ApiIgnore @AuthenticationPrincipal final User user) {
        final AccountDto createdAccountDto = accountService.create(accountForm, user);
        if (createdAccountDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(createdAccountDto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> update(@RequestBody final AccountForm accountForm,
                                             @PathVariable final String id,
                                             @ApiIgnore @AuthenticationPrincipal final User user) {
        final AccountDto updatedAccountDto = accountService.update(accountForm, id, user);
        if (updatedAccountDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(updatedAccountDto);
    }

    @PutMapping(value = "/{id}/user/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addUser(@PathVariable final String username,
                                          @PathVariable final String id,
                                          @ApiIgnore @AuthenticationPrincipal final User user) {

        if (!accountService.addUser(id, username, user))
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(username + " added.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable final String id) {
        accountService.delete(id);
        return ResponseEntity.ok().build();
    }
}
