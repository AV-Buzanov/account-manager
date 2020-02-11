package test.buzanov.accountmanager.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.entity.User;
import test.buzanov.accountmanager.service.IAccountService;

import java.util.Collection;

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
    public ResponseEntity<Collection<AccountDto>> findAll(@RequestParam(value = "page", defaultValue = "0") final int page,
                                                          @RequestParam(value = "size", defaultValue = "100") final int size,
                                                          @AuthenticationPrincipal User user) throws Exception {
        return ResponseEntity.ok(accountService.findAll(page, size, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> findOne(@PathVariable final String id) throws Exception {
        final AccountDto accountDto = accountService.findOne(id);
        if (accountDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(accountDto);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> create(@RequestBody final AccountDto accountDto,
                                             @AuthenticationPrincipal User user) throws Exception {
        final AccountDto createdAccountDto = accountService.create(accountDto, user);
        if (createdAccountDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(createdAccountDto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> update(@RequestBody final AccountDto accountDto) throws Exception {
        final AccountDto updatedAccountDto = accountService.update(accountDto);
        if (updatedAccountDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(updatedAccountDto);
    }

    @PutMapping(value = "/{id}/user/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addUser(@PathVariable final String username,
                                          @PathVariable final String id) throws Exception {

        if (!accountService.addUser(id, username))
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(username + " added.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable final String id) throws Exception {
        accountService.delete(id);
        return ResponseEntity.ok().build();
    }
}
