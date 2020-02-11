package test.buzanov.accountmanager.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.service.ITransactionService;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collection;

/**
 * Класс публикует REST сервис для управления сущностью Transaction.
 * @author Aleksey Buzanov
 */

@RestController
@RequestMapping(value = "/transactions")
public class TransactionRestController {
    @NotNull
    private final ITransactionService transactionService;

    public TransactionRestController(@NotNull @Qualifier(value = "transactionService") final ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Collection<TransactionDto>> findAllByAccount(@PathVariable final String id,
                                                                       @RequestParam(value = "page", defaultValue = "0") final int page,
                                                                       @RequestParam(value = "size", defaultValue = "100") final int size) throws Exception {
        return ResponseEntity.ok(transactionService.findAllByAccount(id, page, size));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Collection<TransactionDto>> findAllByCategory(@PathVariable final String id,
                                                                        @RequestParam(value = "page", defaultValue = "0") final int page,
                                                                        @RequestParam(value = "size", defaultValue = "100") final int size) throws Exception {
        return ResponseEntity.ok(transactionService.findAllByCategory(id, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> findOne(@PathVariable final String id) throws Exception {
        final TransactionDto transactionDto = transactionService.findOne(id);
        if (transactionDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(transactionDto);
    }


    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> create(@RequestBody final TransactionDto transactionDTO) throws Exception {
        final TransactionDto createdTransactionDto = transactionService.create(transactionDTO);
        if (createdTransactionDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(createdTransactionDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable final String id) throws Exception {
        transactionService.delete(id);
        return ResponseEntity.ok().build();
    }
}