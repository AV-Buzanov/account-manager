package test.buzanov.accountmanager.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.enumurated.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * DTO объект для сущности Transaction.
 * @author Aleksey Buzanov
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionForm {
    @Nullable
    private String name;

    @Nullable
    private String description;

    @NotNull
    private String accountId;

    @NotNull
    private String categoryId;

    @Nullable
    private LocalDateTime date;

    @NotNull
    private BigDecimal sum;
}
