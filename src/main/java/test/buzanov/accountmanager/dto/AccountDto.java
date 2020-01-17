package test.buzanov.accountmanager.dto;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    @Nullable
    private String id = UUID.randomUUID().toString();

    @Nullable
    private Date creation;

    @Nullable
    private BigDecimal balance;
}
