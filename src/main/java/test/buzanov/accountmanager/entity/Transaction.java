package test.buzanov.accountmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.entity.Account;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "app_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends AbstractEntity {

    @Nullable
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @Nullable
    private String description;

    private int sum;
}
