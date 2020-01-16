package test.buzanov.accountmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class TransactionDto {
    @Nullable
    private String id = UUID.randomUUID().toString();

    @Nullable
    private String accountId;

    @Nullable
    private String description;

    @Nullable
    private Date creation;

    private int sum;
}
