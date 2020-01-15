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
import test.buzanov.accountmanager.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    private String accountId;

    @Before
    public void before() {
        final AccountDto accountDto = new AccountDto();
        accountDto.setBalance(2000);
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
    public void testing() {
        ExecutorService executorService = Executors.newFixedThreadPool(205);
        List<Future<String>> futures = new ArrayList<>();
        Callable<String> callable = new Callable<String>() {
            public String call() throws Exception {
                HttpEntity<TransactionDto> entity = new HttpEntity<TransactionDto>(new TransactionDto());
                entity.getBody().setAccountId(accountId);
                entity.getBody().setSum(-10);
                ResponseEntity<TransactionDto> response = restTemplate.exchange("/api/transaction/create", HttpMethod.PUT, entity, TransactionDto.class);
                return String.valueOf(response.getStatusCodeValue());
            }
        };

        for (int i = 0; i < 205; i++) {
            futures.add(executorService.submit(callable));
        }

        futures.forEach(s-> {
            try {
                System.out.println(s.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        boolean as = futures.stream().filter(s -> {
            try {
                return "500".equals(s.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return false;
            }
        }).count() == 5;
        Assert.assertTrue("Fail", as);
    }
}
