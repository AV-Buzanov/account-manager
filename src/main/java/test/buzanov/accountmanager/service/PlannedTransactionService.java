package test.buzanov.accountmanager.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import test.buzanov.accountmanager.dto.PlannedTransactionDto;
import test.buzanov.accountmanager.dto.converter.IPlannedTransactionDtoConverter;
import test.buzanov.accountmanager.repository.PlannedTransactionRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Класс реализует бизнесс логику и валидацию данных для сущности PlannedTransaction.
 *
 * @author Aleksey Buzanov
 */

@Service
public class PlannedTransactionService implements IPlannedTransactionService {

    @NotNull
    private final PlannedTransactionRepository plannedTransactionRepository;

    @NotNull
    private final IPlannedTransactionDtoConverter plannedTransactionDtoConverter;

    public PlannedTransactionService(@NotNull final PlannedTransactionRepository plannedTransactionRepository,
                                     @NotNull final IPlannedTransactionDtoConverter plannedTransactionDtoConverter) {
        this.plannedTransactionRepository = plannedTransactionRepository;
        this.plannedTransactionDtoConverter = plannedTransactionDtoConverter;
    }

    @NotNull
    public Collection<PlannedTransactionDto> findAll(int page, int size) {
        return plannedTransactionRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(plannedTransactionDtoConverter::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @NotNull
    public Collection<PlannedTransactionDto> findAllByAccount(@Nullable final String id,
                                                              int page, int size) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        return plannedTransactionRepository.findAllByAccountId(id, PageRequest.of(page, size))
                .stream()
                .map(plannedTransactionDtoConverter::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @NotNull
    public Collection<PlannedTransactionDto> findAllByCategory(@Nullable final String id,
                                                               int page, int size) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        return plannedTransactionRepository.findAllByCategoryId(id, PageRequest.of(page, size))
                .stream()
                .map(plannedTransactionDtoConverter::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @Nullable
    public PlannedTransactionDto findOne(@Nullable final String id) throws Exception {
        if (id == null || id.isEmpty()) throw new Exception("Id can't by empty or null");
        return plannedTransactionDtoConverter.toTransactionDTO(plannedTransactionRepository.findById(id).orElseThrow(null));
    }

    @Nullable
    public PlannedTransactionDto create(@Nullable final PlannedTransactionDto transactionDto) throws Exception {
        if (transactionDto == null || transactionDto.getId() == null || transactionDto.getId().isEmpty())
            throw new Exception("Id can't be empty or null");
        if (transactionDto.getAccountId() == null || transactionDto.getAccountId().isEmpty())
            throw new Exception("AccountId can't be empty or null");
        if (transactionDto.getSum() == null || transactionDto.getSum().compareTo(BigDecimal.ZERO) <= 0)
            throw new Exception("Sum can't be null, negative or 0");
        if (plannedTransactionRepository.existsById(transactionDto.getId()))
            throw new Exception("Transaction id already exists");

        return plannedTransactionDtoConverter.toTransactionDTO(
                plannedTransactionRepository.saveAndFlush(
                        plannedTransactionDtoConverter.toTransactionEntity(transactionDto)));

    }

    public void delete(String id) throws Exception {
        if (id == null || id.isEmpty()) throw new NullPointerException("Id can't by empty or null");
        plannedTransactionRepository.deleteById(id);
    }
}
