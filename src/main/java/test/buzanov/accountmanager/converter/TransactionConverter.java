package test.buzanov.accountmanager.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.entity.Transaction;
import test.buzanov.accountmanager.form.TransactionForm;

import java.math.RoundingMode;

/**
 * Класс реализует перевод сущности Transaction в DTO объект и обратно.
 *
 * @author Aleksey Buzanov
 */

@Component
public class TransactionConverter implements ITransactionConverter {

    @Nullable
    public Transaction toTransactionEntity(@Nullable final TransactionForm transactionForm) {
        if (transactionForm == null) return null;
        @NotNull final Transaction transaction = new Transaction();
        if (transactionForm.getSum() != null)
            transaction.setSum(transactionForm.getSum().setScale(2, RoundingMode.DOWN));
        transaction.setName(transactionForm.getName());
        transaction.setDescription(transactionForm.getDescription());
        transaction.setDate(transactionForm.getDate());
        transaction.setTransactionType(transactionForm.getTransactionType());
        return transaction;
    }

    @Nullable
    public TransactionDto toTransactionDTO(@Nullable final Transaction transaction) {
        if (transaction == null) return null;
        @NotNull final TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setSum(transaction.getSum());
        transactionDto.setCreation(transaction.getCreation());
        transactionDto.setDate(transaction.getDate());
        if (transaction.getAccount() != null)
            transactionDto.setAccountId(transaction.getAccount().getId());
        if (transaction.getCategory() != null)
            transactionDto.setCategoryId(transaction.getCategory().getId());
        transactionDto.setName(transaction.getName());
        transactionDto.setDescription(transaction.getDescription());
        transactionDto.setTransactionType(transaction.getTransactionType());
        return transactionDto;
    }
}
