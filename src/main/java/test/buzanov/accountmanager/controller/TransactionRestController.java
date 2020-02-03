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
@RequestMapping(value = "/transaction")
public class TransactionRestController {
    @NotNull
    private final ITransactionService transactionService;

    public TransactionRestController(@NotNull @Qualifier(value = "transactionService") final ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/findByAccount/{id}")
    public ResponseEntity<Collection<TransactionDto>> findAllByAccount(@PathVariable final String id,
                                                                       @RequestHeader("page") int page,
                                                                       @RequestHeader("size") int size) throws Exception {
        return ResponseEntity.ok(transactionService.findAllByAccount(id, page, size));
    }

    @GetMapping("/findByCategory/{id}")
    public ResponseEntity<Collection<TransactionDto>> findAllByCategory(@PathVariable final String id,
                                                                        @RequestHeader("page") int page,
                                                                        @RequestHeader("size") int size) throws Exception {
        return ResponseEntity.ok(transactionService.findAllByCategory(id, page, size));
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable final String id) throws Exception {
        transactionService.delete(id);
        return ResponseEntity.ok().build();
    }
}