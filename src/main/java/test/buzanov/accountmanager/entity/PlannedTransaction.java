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
import java.time.LocalDateTime;

/**
 * Сущность PlannedTransaction (запланированная операция по счету)
 *
 * @author Aleksey Buzanov
 */

@Entity
@Data
@Table(name = "app_planned_transaction")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"account", "category"})
public class PlannedTransaction extends AbstractEntity {

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Nullable
    private String description;

    @Nullable
    private LocalDateTime date;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private BigDecimal sum;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(value = EnumType.STRING)
    private Frequency frequency;

    private boolean auto;

    public enum Frequency {
        ONCE,
        EVERYDAY,
        EVERYWEEK,
        EVERYMONTH
    }
}
