package test.buzanov.accountmanager.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import test.buzanov.accountmanager.enumurated.TransactionType;

import java.util.UUID;

/**
 * DTO объект для сущности Category.
 *
 * @author Aleksey Buzanov
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryForm {

    @Nullable
    private String name;

    @Nullable
    private String description;

    @Nullable
    private String parentId;

    @Nullable
    private String accountId;

    private TransactionType transactionType;

}
