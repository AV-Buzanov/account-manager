package test.buzanov.accountmanager.dto.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.entity.Transaction;
import test.buzanov.accountmanager.repository.AccountRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class TransactionDtoConverter implements ITransactionDtoConverter{

    @Nullable
    public Transaction toTransactionEntity(@Nullable final TransactionDto transactionDto) {
        if (transactionDto == null || transactionDto.getId() == null) return null;
        @NotNull final Transaction transaction = new Transaction();
        transaction.setId(transactionDto.getId());
        transaction.setSum(transactionDto.getSum());
        transaction.getSum().setScale(2, RoundingMode.DOWN);
        transaction.setDescription(transactionDto.getDescription());
        transaction.setTransactionType(transactionDto.getTransactionType());
        return transaction;
    }

    @Nullable
    public TransactionDto toTransactionDTO(@Nullable final Transaction transaction) {
        if (transaction == null) return null;
        @NotNull final TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setSum(transaction.getSum());
        transactionDto.setCreation(transaction.getCreation());
        if (transaction.getAccount() != null)
            transactionDto.setAccountId(transaction.getAccount().getId());
        transactionDto.setDescription(transaction.getDescription());
        transactionDto.setTransactionType(transaction.getTransactionType());
        return transactionDto;
    }
}
