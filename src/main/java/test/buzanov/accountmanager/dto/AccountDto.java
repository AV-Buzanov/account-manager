package test.buzanov.accountmanager.dto;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountDto{

    @Nullable
    private String id;

    @Nullable
    private Date creation;

    private int balance;
}
