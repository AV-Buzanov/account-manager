package test.buzanov.accountmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.enumurated.TransactionType;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Сущность Transaction (операция по счету)
 * @author Aleksey Buzanov
 */

@Entity
@Data
@Table(name = "app_transaction")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "account")
public class Transaction extends AbstractEntity {

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Nullable
    private String description;

    private BigDecimal sum;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;
}
