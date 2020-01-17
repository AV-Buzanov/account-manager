package test.buzanov.accountmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.enumurated.TransactionType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@ToString
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
