package test.buzanov.accountmanager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.enumurated.TransactionType;
import test.buzanov.accountmanager.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    private final static int NUMBER_OF_THREADS = 500;

    private final static int NUMBER_OF_FAIL_THREADS = 100;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    private String accountId;

    @Before
    public void before() {
        final AccountDto accountDto = new AccountDto();
        accountDto.setBalance(BigDecimal.valueOf((NUMBER_OF_THREADS - NUMBER_OF_FAIL_THREADS) * 10 + 2));
        HttpEntity<AccountDto> entity = new HttpEntity<AccountDto>(accountDto);
        ResponseEntity<AccountDto> response = restTemplate.exchange("/api/account/create", HttpMethod.PUT, entity, AccountDto.class);
        accountId = response.getBody().getId();
        System.out.println(response.getBody().toString());
    }

    @After
    public void after() {
        accountRepository.deleteById(accountId);
    }

    @Test
    public void testWithdrawOver() {
        final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        final List<Future<String>> futures = new ArrayList<>();
        final Callable<String> callable = () -> {
            final HttpEntity<TransactionDto> entity = new HttpEntity<TransactionDto>(new TransactionDto());
            entity.getBody().setAccountId(accountId);
            entity.getBody().setSum(BigDecimal.valueOf(10));
            entity.getBody().setTransactionType(TransactionType.WITHDRAW);
            final ResponseEntity<TransactionDto> response = restTemplate
                    .exchange("/api/transaction/create", HttpMethod.PUT, entity, TransactionDto.class);
            return String.valueOf(response.getStatusCodeValue());
        };

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            futures.add(executorService.submit(callable));
        }

        long countOfErrorRequests = futures.stream().filter(s -> {
            try {
                return "500".equals(s.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return false;
            }
        }).count();

        Assert.assertEquals("Count of error request incorrect: ", NUMBER_OF_FAIL_THREADS, countOfErrorRequests);

        final ResponseEntity<AccountDto> responseAccount = restTemplate
                .exchange("/api/account/find/" + accountId, HttpMethod.GET, null, AccountDto.class);
        Assert.assertNotNull("responseAccount body is null", responseAccount.getBody());

        int accountBalance = responseAccount.getBody().getBalance().intValue();

        Assert.assertEquals("Account balance incorrect: " + accountBalance, 2, accountBalance);
    }
}
