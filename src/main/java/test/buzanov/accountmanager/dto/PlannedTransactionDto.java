package test.buzanov.accountmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.entity.PlannedTransaction;
import test.buzanov.accountmanager.enumurated.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * DTO объект для сущности PlannedTransaction.
 *
 * @author Aleksey Buzanov
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlannedTransactionDto {
    @Nullable
    private String id = UUID.randomUUID().toString();

    @Nullable
    private String accountId;

    @Nullable
    private String categoryId;

    @Nullable
    private LocalDateTime date;

    @Nullable
    private String description;

    @Nullable
    private Date creation;

    @Nullable
    private BigDecimal sum;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    private PlannedTransaction.Frequency frequency;

    private boolean auto;
}
