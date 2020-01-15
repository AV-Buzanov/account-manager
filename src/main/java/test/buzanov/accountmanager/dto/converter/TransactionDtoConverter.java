package test.buzanov.accountmanager.dto.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.entity.Account;
import test.buzanov.accountmanager.entity.Transaction;
import test.buzanov.accountmanager.repository.AccountRepository;

@Component
public class TransactionDtoConverter {
    @NotNull
    @Autowired
    private AccountRepository accountRepository;

    @Nullable
    public Transaction toTransactionEntity(@Nullable final TransactionDto transactionDto) {
        if (transactionDto == null) return null;
        @NotNull final Transaction transaction = new Transaction();
        transaction.setId(transactionDto.getId());
//        if (transactionDto.getAccountId() != null)
//            transaction.setAccount(accountRepository.findById(transactionDto.getAccountId()).orElse(null));
        transaction.setSum(transactionDto.getSum());
        transaction.setDescription(transactionDto.getDescription());
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
        return transactionDto;
    }
}
