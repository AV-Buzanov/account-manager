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
        final TransactionDto transactionDto = transactionService.findOne(id);
        if (transactionDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(transactionDto);
    }

    @PutMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> create(@RequestBody final TransactionDto transactionDTO) throws Exception {
        final TransactionDto createdTransactionDto = transactionService.create(transactionDTO);
        if (createdTransactionDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(createdTransactionDto);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> update(@RequestBody final TransactionDto transactionDTO) throws Exception {
        final TransactionDto updatedTransactionDto = transactionService.update(transactionDTO);
        if (updatedTransactionDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(updatedTransactionDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable final String id) throws Exception {
        transactionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
