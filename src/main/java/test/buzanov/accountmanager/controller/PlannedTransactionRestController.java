package test.buzanov.accountmanager.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.buzanov.accountmanager.dto.PlannedTransactionDto;
import test.buzanov.accountmanager.service.IPlannedTransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Класс публикует REST сервис для управления сущностью PlannedTransaction.
 *
 * @author Aleksey Buzanov
 */

@RestController
@RequestMapping(value = "/transaction/planned")
public class PlannedTransactionRestController {
    @NotNull
    private final IPlannedTransactionService plannedTransactionService;

    public PlannedTransactionRestController(@NotNull @Qualifier(value = "plannedTransactionService") final IPlannedTransactionService plannedTransactionService) {
        this.plannedTransactionService = plannedTransactionService;
    }

    @GetMapping("/findByAccount/{id}")
    public ResponseEntity<Collection<PlannedTransactionDto>> findAllByAccount(@PathVariable final String id,
                                                                              @RequestHeader("page") int page,
                                                                              @RequestHeader("size") int size) throws Exception {
        return ResponseEntity.ok(plannedTransactionService.findAllByAccount(id, page, size));
    }

    @GetMapping("/findByCategory/{id}")
    public ResponseEntity<Collection<PlannedTransactionDto>> findAllByCategory(@PathVariable final String id,
                                                                               @RequestHeader("page") int page,
                                                                               @RequestHeader("size") int size) throws Exception {
        return ResponseEntity.ok(plannedTransactionService.findAllByCategory(id, page, size));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<PlannedTransactionDto> findOne(@PathVariable final String id) throws Exception {
        final PlannedTransactionDto transactionDto = plannedTransactionService.findOne(id);
        if (transactionDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(transactionDto);
    }

    @GetMapping("/sumOnDate/{id}")
    public ResponseEntity<BigDecimal> plannedSumOnDate(@PathVariable final String id, @RequestHeader("date") String date) throws Exception {
        return ResponseEntity.ok(plannedTransactionService.getPlannedSumOnDate(id, LocalDate.parse(date)));
    }

    @PutMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlannedTransactionDto> create(@RequestBody final PlannedTransactionDto transactionDTO) throws Exception {
        final PlannedTransactionDto createdTransactionDto = plannedTransactionService.create(transactionDTO);
        if (createdTransactionDto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(createdTransactionDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable final String id) throws Exception {
        plannedTransactionService.delete(id);
        return ResponseEntity.ok().build();
    }
}