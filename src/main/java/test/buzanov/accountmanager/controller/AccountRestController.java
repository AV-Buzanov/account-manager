package test.buzanov.accountmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.service.AccountService;

import java.util.Collection;

@RestController
@RequestMapping(value = "/account")
public class AccountRestController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/findAll")
    public ResponseEntity<Collection<AccountDto>> findAll() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<AccountDto> findOne(@PathVariable final String id) throws Exception {
        final AccountDto accountDto = accountService.findOne(id);
        if (accountDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(accountDto);
    }

    @PutMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> create(@RequestBody final AccountDto accountDto) throws Exception {
        final AccountDto createdAccountDto = accountService.create(accountDto);
        if (createdAccountDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(createdAccountDto);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> update(@RequestBody final AccountDto accountDto) throws Exception {
        final AccountDto updatedAccountDto = accountService.update(accountDto);
        if (updatedAccountDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(updatedAccountDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable final String id) throws Exception {
        accountService.delete(id);
        return ResponseEntity.ok().build();
    }
}
