package test.buzanov.accountmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
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
@EqualsAndHashCode(callSuper = true, exclude = {"transactions","users"})
public class Account extends AbstractEntity {

    private String name;

    private String description;

    @NotNull
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Transaction> transactions = new HashSet<>();
    @NotNull
    private BigDecimal balance = new BigDecimal("0").setScale(2, RoundingMode.DOWN);

    @NotNull
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "account_user",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    public void addBalance(BigDecimal sum) {
        this.balance = this.balance.add(sum);
    }

    public void subBalance(BigDecimal sum) {
        this.balance = this.balance.subtract(sum);
    }
}
