package test.buzanov.accountmanager.dto;

import lombok.AllArgsConstructor;
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
public class CategoryDto {
    @Nullable
    private String id = UUID.randomUUID().toString();

    @Nullable
    private String name;

    @Nullable
    private String description;

    @Nullable
    private String parentId;

    @NotNull
    private TransactionType transactionType;
}
