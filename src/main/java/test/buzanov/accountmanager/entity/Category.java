package test.buzanov.accountmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.enumurated.TransactionType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность Category (категория операции)
 *
 * @author Aleksey Buzanov
 */

@Entity
@Data
@Table(name = "app_category")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"childs"})
public class Category extends AbstractEntity {

    @Nullable
    private String name;

    @Nullable
    private String description;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @NotNull
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Category> childs = new HashSet<>();

    @NotNull
    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private Set<Transaction> transactions = new HashSet<>();

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;
}
