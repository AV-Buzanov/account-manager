package test.buzanov.accountmanager.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.service.TransactionService;

import java.util.Collection;


@RestController
@RequestMapping(value = "/transaction")
public class TransactionRestController {
    @Autowired
    @NotNull
    private TransactionService transactionService;

    @GetMapping("/findAll/{id}")
    public ResponseEntity<Collection<TransactionDto>> findAll(@PathVariable final String id) throws Exception {
        return ResponseEntity.ok(transactionService.findAllByAccount(id));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<TransactionDto> findOne(@PathVariable final String id) throws Exception {
        return ResponseEntity.ok(transactionService.findOne(id));
    }

    @PutMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> create(@RequestBody final TransactionDto transactionDTO) throws Exception {
        ResponseEntity<TransactionDto> entity = ResponseEntity.ok(transactionService
                .create(transactionDTO));
        return entity;
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> update(@RequestBody final TransactionDto transactionDTO) throws Exception {
        return ResponseEntity.ok(transactionService
                .update(transactionDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable final String id) throws Exception {
        transactionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
