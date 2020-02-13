package test.buzanov.accountmanager;

import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.junit4.SpringRunner;
import test.buzanov.accountmanager.dto.AccountDto;
import test.buzanov.accountmanager.dto.CategoryDto;
import test.buzanov.accountmanager.dto.TransactionDto;
import test.buzanov.accountmanager.enumurated.TransactionType;
import test.buzanov.accountmanager.form.AccountForm;
import test.buzanov.accountmanager.form.CategoryForm;
import test.buzanov.accountmanager.form.TransactionForm;
import test.buzanov.accountmanager.form.UserForm;
import test.buzanov.accountmanager.repository.AccountRepository;
import test.buzanov.accountmanager.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RelationsTest {
    private final static int NUMBER_OF_THREADS = 2000;

    private final static int NUMBER_OF_FAIL_THREADS = 100;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    private String categoryId;

    private String accountId;

    private HttpHeaders tokenHeader;

    @Before
    public void before() {
        final UserForm userForm = UserForm.builder()
                .username("Test").password("password").name("name").build();

        final HttpEntity<UserForm> userEntity = new HttpEntity<>(userForm);
        final ResponseEntity<String> regResponse = restTemplate.exchange("/api/auth/registration", HttpMethod.POST, userEntity, String.class);
        Assert.assertEquals("User registration fail", 200, regResponse.getStatusCodeValue());

        final ResponseEntity<String> authResponse = restTemplate.exchange("/login", HttpMethod.POST, userEntity, String.class);
        Assert.assertEquals("User auth fail", 200, authResponse.getStatusCodeValue());
        Assert.assertNotNull("Token not found", authResponse.getHeaders().get("Authorization"));

        String token = authResponse.getHeaders().get("Authorization").get(0);
        tokenHeader = new HttpHeaders();
        tokenHeader.add("Authorization", token);

        final HttpEntity<AccountForm> accountEntity = new HttpEntity<>(AccountForm.builder().name("Test").description("Test").build(), tokenHeader);
        final ResponseEntity<AccountDto> account1Response = restTemplate.exchange("/api/accounts/", HttpMethod.POST, accountEntity, AccountDto.class);
        Assert.assertEquals("Account registration fail", 200, account1Response.getStatusCodeValue());
        Assert.assertNotNull("Account body not found", account1Response.getBody());

        final HttpEntity<AccountForm> account2Entity = new HttpEntity<>(AccountForm.builder().name("Test2").description("Test2").build(), tokenHeader);
        final ResponseEntity<AccountDto> account2Response = restTemplate.exchange("/api/accounts/", HttpMethod.POST, account2Entity, AccountDto.class);
        Assert.assertEquals("Account2 registration fail", 200, account2Response.getStatusCodeValue());
        Assert.assertNotNull("Account2 body not found", account2Response.getBody());

    }


    @After
    public void after() {
        userRepository.deleteUserByUsername("Test");
    }

        @Test
    public void accounts() {

            final ResponseEntity<AccountDto[]> account3Response = restTemplate.exchange("/api/accounts/", HttpMethod.GET
                    , new HttpEntity<>(tokenHeader), AccountDto[].class );
            Assert.assertEquals("Accounts get fail", 200, account3Response.getStatusCodeValue());
            Assert.assertNotNull("Accounts get body not found", account3Response.getBody());
            Assert.assertEquals("Count of accounts incorrect.", account3Response.getBody().length, 2);
            Assert.assertTrue("Account doesn't contains username."
                    ,account3Response.getBody()[0].getUsers().contains("Test")
                            &&account3Response.getBody()[1].getUsers().contains("Test"));

            String account1Id = account3Response.getBody()[0].getId();
            String account2Id = account3Response.getBody()[1].getId();

            final HttpEntity<CategoryForm> categoryEntity = new HttpEntity<>(CategoryForm.builder().accountId(account1Id).name("Test")
                    .transactionType(TransactionType.DEPOSIT).build(), tokenHeader);
            final ResponseEntity<CategoryDto> category1Response = restTemplate.exchange("/api/categories/", HttpMethod.POST, categoryEntity, CategoryDto.class);
            Assert.assertEquals("Category registration fail", 200, category1Response.getStatusCodeValue());
            Assert.assertNotNull("Category body not found", category1Response.getBody());
            Assert.assertNotNull("AccountId in category body not found", category1Response.getBody().getAccountId());

        }



//
//    @Test
//    public void testWithdrawOver() {
//
//        final HttpEntity<TransactionDto> entityDeposit = new HttpEntity<TransactionDto>(new TransactionDto());
//        entityDeposit.getBody().setAccountId(accountId);
//        entityDeposit.getBody().setSum(BigDecimal.valueOf((NUMBER_OF_THREADS - NUMBER_OF_FAIL_THREADS) * 10).add(BigDecimal.valueOf(2.5)));
//        entityDeposit.getBody().setTransactionType(TransactionType.DEPOSIT);
//        final ResponseEntity<TransactionDto> responseDeposit = restTemplate
//                .exchange("/api/transaction/create", HttpMethod.PUT, entityDeposit, TransactionDto.class);
//        Assert.assertEquals("Deposit transaction fail", 200, responseDeposit.getStatusCodeValue());
//
//        final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
//        final List<Future<String>> futures = new ArrayList<>();
//        final Callable<String> callable = () -> {
//            final HttpEntity<TransactionDto> entity = new HttpEntity<TransactionDto>(new TransactionDto());
//            entity.getBody().setAccountId(accountId);
//            entity.getBody().setSum(BigDecimal.valueOf(10));
//            entity.getBody().setTransactionType(TransactionType.WITHDRAW);
//            final ResponseEntity<TransactionDto> response = restTemplate
//                    .exchange("/api/transaction/create", HttpMethod.PUT, entity, TransactionDto.class);
//            return String.valueOf(response.getStatusCodeValue());
//        };
//
//        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
//            futures.add(executorService.submit(callable));
//        }
//
//        long countOfErrorRequests = futures
//                .stream()
//                .filter(s -> {
//                    try {
//                        return !"200".equals(s.get());
//                    } catch (InterruptedException | ExecutionException e) {
//                        e.printStackTrace();
//                        return false;
//                    }
//                }).count();
//
//        Assert.assertEquals("Count of error request incorrect: ", NUMBER_OF_FAIL_THREADS, countOfErrorRequests);
//        final ResponseEntity<AccountDto> responseAccount = restTemplate
//                .exchange("/api/account/find/" + accountId, HttpMethod.GET, null, AccountDto.class);
//        Assert.assertNotNull("responseAccount body is null", responseAccount.getBody());
//        BigDecimal accountBalance = responseAccount.getBody().getBalance();
//        Assert.assertEquals("Account balance incorrect: " + accountBalance
//                , BigDecimal.valueOf(2.5)
//                , accountBalance.stripTrailingZeros());
//    }
//
//    @Test
//    public void testWithdrawAndDeposit() {
//        final HttpEntity<TransactionDto> entityDeposit1 = new HttpEntity<TransactionDto>(new TransactionDto());
//        entityDeposit1.getBody().setAccountId(accountId);
//        entityDeposit1.getBody().setSum(BigDecimal.valueOf(NUMBER_OF_THREADS));
//        entityDeposit1.getBody().setTransactionType(TransactionType.DEPOSIT);
//        final ResponseEntity<TransactionDto> responseDeposit1 = restTemplate
//                .exchange("/api/transaction/create", HttpMethod.PUT, entityDeposit1, TransactionDto.class);
//        Assert.assertEquals("Deposit transaction fail", 200, responseDeposit1.getStatusCodeValue());
//
//        final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
//        final List<Future<String>> futures = new ArrayList<>();
//        final Callable<String> callableDeposit = () -> {
//            final HttpEntity<TransactionDto> entityDeposit = new HttpEntity<TransactionDto>(new TransactionDto());
//            entityDeposit.getBody().setAccountId(accountId);
//            entityDeposit.getBody().setSum(BigDecimal.valueOf(10.3));
//            entityDeposit.getBody().setTransactionType(TransactionType.DEPOSIT);
//            final ResponseEntity<TransactionDto> responseDeposit = restTemplate
//                    .exchange("/api/transaction/create", HttpMethod.PUT, entityDeposit, TransactionDto.class);
//
//            return String.valueOf(responseDeposit.getStatusCodeValue());
//        };
//
//        final Callable<String> callableWithdraw = () -> {
//            final HttpEntity<TransactionDto> entityWithdraw = new HttpEntity<TransactionDto>(new TransactionDto());
//            entityWithdraw.getBody().setAccountId(accountId);
//            entityWithdraw.getBody().setSum(BigDecimal.valueOf(10.5));
//            entityWithdraw.getBody().setTransactionType(TransactionType.WITHDRAW);
//            final ResponseEntity<TransactionDto> responseWithdraw = restTemplate
//                    .exchange("/api/transaction/create", HttpMethod.PUT, entityWithdraw, TransactionDto.class);
//
//            return String.valueOf(responseWithdraw.getStatusCodeValue());
//        };
//
//        for (int i = 0; i < NUMBER_OF_THREADS / 2; i++) {
//            futures.add(executorService.submit(callableDeposit));
//            futures.add(executorService.submit(callableWithdraw));
//        }
//        long countOfErrorRequests = futures
//                .stream()
//                .filter(s -> {
//                    try {
//                        return !"200".equals(s.get());
//                    } catch (InterruptedException | ExecutionException e) {
//                        e.printStackTrace();
//                        return false;
//                    }
//                }).count();
//
//        Assert.assertEquals("Count of error request incorrect: ", 0, countOfErrorRequests);
//        final ResponseEntity<AccountDto> responseAccount = restTemplate
//                .exchange("/api/account/find/" + accountId, HttpMethod.GET, null, AccountDto.class);
//        Assert.assertNotNull("responseAccount body is null", responseAccount.getBody());
//        BigDecimal accountBalance = responseAccount.getBody().getBalance();
//        Assert.assertEquals("Account balance incorrect: "
//                , BigDecimal.valueOf(NUMBER_OF_THREADS)
//                        .subtract(BigDecimal.valueOf(NUMBER_OF_THREADS / 2)
//                                .multiply(BigDecimal.valueOf(0.2)))
//                        .stripTrailingZeros()
//                , accountBalance.stripTrailingZeros());
//    }
}
