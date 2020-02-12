package test.buzanov.accountmanager.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class AccountForm {

    private String name;

    private String description;
}
