package test.buzanov.accountmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность Account (денежный счет)
 * @author Aleksey Buzanov
 */

@Entity
@Data
@Table(name = "app_account")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "transactions")
public class Account extends AbstractEntity {

    @NotNull
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Transaction> transactions = new HashSet<>();
    @NotNull
    private BigDecimal balance = new BigDecimal("0").setScale(2, RoundingMode.DOWN);

    public void addBalance(BigDecimal sum) {
        this.balance = this.balance.add(sum);
    }

    public void subBalance(BigDecimal sum) {
        this.balance = this.balance.subtract(sum);
    }
}
