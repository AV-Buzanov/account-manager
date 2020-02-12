package test.buzanov.accountmanager.dto;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * DTO объект для сущности Account.
 * @author Aleksey Buzanov
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @Nullable
    private String id;
    @Nullable
    private Date creation;

    private String name;

    private String description;

    @Nullable
    private BigDecimal balance;
}
