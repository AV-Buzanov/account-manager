package test.buzanov.accountmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 *
 * @author Aleksey Buzanov
 */

@Entity
@Data
@Table(name = "app_account")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"transactions", "users", "categories"})
public class Account extends AbstractEntity {

    private String name;

    private String description;

    @NotNull
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Transaction> transactions = new HashSet<>();

    @NotNull
    private BigDecimal balance = new BigDecimal("0").setScale(2, RoundingMode.DOWN);

    @NotNull
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(name = "user_account",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @NotNull
    @OneToMany(mappedBy = "account", cascade = {CascadeType.ALL})
    private Set<Category> categories = new HashSet<>();

    public void addBalance(BigDecimal sum) {
        this.balance = this.balance.add(sum);
    }

    public void subBalance(BigDecimal sum) {
        this.balance = this.balance.subtract(sum);
    }
}
