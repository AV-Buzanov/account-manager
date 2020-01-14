package test.buzanov.accountmanager.dto;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class TransactionDto {
    @Nullable
    private String id;

    @Nullable
    private String accountId;

    @Nullable
    private String description;

    @Nullable
    private Date creation;

    private int sum;
}
