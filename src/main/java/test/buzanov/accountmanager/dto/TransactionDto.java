package test.buzanov.accountmanager.dto;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.enumurated.TransactionType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * DTO объект для сущности Transaction.
 * @author Aleksey Buzanov
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    @Nullable
    private String id = UUID.randomUUID().toString();

    @Nullable
    private String accountId;

    @Nullable
    private String description;

    @Nullable
    private Date creation;

    @Nullable
    private BigDecimal sum;

    @NotNull
    private TransactionType transactionType;
}
