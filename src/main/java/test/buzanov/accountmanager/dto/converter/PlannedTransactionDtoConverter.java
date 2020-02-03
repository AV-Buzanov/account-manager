package test.buzanov.accountmanager.dto.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import test.buzanov.accountmanager.dto.PlannedTransactionDto;
import test.buzanov.accountmanager.entity.PlannedTransaction;

import java.math.RoundingMode;

/**
 * Класс реализует перевод сущности Transaction в DTO объект и обратно.
 *
 * @author Aleksey Buzanov
 */

@Component
public class PlannedTransactionDtoConverter implements IPlannedTransactionDtoConverter {

    @Nullable
    public PlannedTransaction toTransactionEntity(@Nullable final PlannedTransactionDto plannedTransactionDto) {
        if (plannedTransactionDto == null || plannedTransactionDto.getId() == null) return null;
        @NotNull final PlannedTransaction plannedTransaction = new PlannedTransaction();
        plannedTransaction.setId(plannedTransactionDto.getId());
        plannedTransaction.setSum(plannedTransactionDto.getSum());
        plannedTransaction.getSum().setScale(2, RoundingMode.DOWN);
        plannedTransaction.setDescription(plannedTransactionDto.getDescription());
        plannedTransaction.setTransactionType(plannedTransactionDto.getTransactionType());
        plannedTransaction.setFrequency(plannedTransactionDto.getFrequency());
        plannedTransaction.setAuto(plannedTransactionDto.isAuto());
        plannedTransaction.setDate(plannedTransactionDto.getDate());
        return plannedTransaction;
    }

    @Nullable
    public PlannedTransactionDto toTransactionDTO(@Nullable final PlannedTransaction plannedTransaction) {
        if (plannedTransaction == null) return null;
        @NotNull final PlannedTransactionDto plannedTransactionDto = new PlannedTransactionDto();
        plannedTransactionDto.setId(plannedTransaction.getId());
        plannedTransactionDto.setSum(plannedTransaction.getSum());
        plannedTransactionDto.setCreation(plannedTransaction.getCreation());
        if (plannedTransaction.getAccount() != null)
            plannedTransactionDto.setAccountId(plannedTransaction.getAccount().getId());
        if (plannedTransaction.getCategory() != null)
            plannedTransactionDto.setCategoryId(plannedTransaction.getCategory().getId());
        plannedTransactionDto.setDescription(plannedTransaction.getDescription());
        plannedTransactionDto.setTransactionType(plannedTransaction.getTransactionType());
        plannedTransactionDto.setFrequency(plannedTransaction.getFrequency());
        plannedTransactionDto.setAuto(plannedTransaction.isAuto());
        plannedTransactionDto.setDate(plannedTransaction.getDate());
        return plannedTransactionDto;
    }
}
