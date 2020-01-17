package test.buzanov.accountmanager.entity;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "app_account")
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractEntity {

    @NotNull
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Transaction> transactions = new HashSet<>();

    private BigDecimal balance;

    public void addBalance(BigDecimal sum) {
        this.balance = this.balance.add(sum);
    }

    public void subBalance(BigDecimal sum) {
        this.balance = this.balance.subtract(sum);
    }
}
