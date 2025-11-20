package com.example.wallet;

import com.example.wallet.dto.AccountDto;
import com.example.wallet.dto.AccountOperationRequestDto;
import com.example.wallet.model.OperationType;
import com.example.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MainApplicationTests {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.1");


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WalletService walletService;

    private final String BASE_URL = "/api/v1/wallets";

    private AccountDto wallet;

    @BeforeEach
    void setUp() {
        wallet = walletService.createWallet();
    }

    @Test
    void testDeposit() {
        AccountOperationRequestDto request = new AccountOperationRequestDto();
        request.setWalletId(wallet.getId());
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(new BigDecimal("1000.00"));

        ResponseEntity<AccountDto> response = restTemplate.postForEntity(
                BASE_URL, request, AccountDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(wallet.getId());
        assertThat(response.getBody().getBalance()).isEqualTo(request.getAmount());
    }

    @Test
    void testWithdraw() {
        AccountOperationRequestDto request = new AccountOperationRequestDto();
        request.setWalletId(wallet.getId());
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(new BigDecimal("1000.00"));

        ResponseEntity<AccountDto> responseDeposit = restTemplate.postForEntity(
                BASE_URL, request, AccountDto.class);
        assertThat(responseDeposit.getBody()).isNotNull();

        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(new BigDecimal("600.00"));

        ResponseEntity<AccountDto> responseWithdraw = restTemplate.postForEntity(
                BASE_URL, request, AccountDto.class);

        assertThat(responseWithdraw.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseWithdraw.getBody()).isNotNull();
        BigDecimal expectedBalance = responseDeposit.getBody().getBalance().subtract(request.getAmount());
        assertThat(responseWithdraw.getBody().getId()).isEqualTo(wallet.getId());
        assertThat(responseWithdraw.getBody().getBalance()).isEqualTo(expectedBalance);
    }

    @Test
    void testGetWallet() {
        String getUrl = BASE_URL + "/" + wallet.getId();

        AccountOperationRequestDto request = new AccountOperationRequestDto();
        request.setWalletId(wallet.getId());
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(new BigDecimal("1000.00"));

        ResponseEntity<AccountDto> responseDeposit = restTemplate.postForEntity(
                BASE_URL, request, AccountDto.class);
        assertThat(responseDeposit.getBody()).isNotNull();

        ResponseEntity<AccountDto> response = restTemplate.getForEntity(
                getUrl, AccountDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(wallet.getId());
        assertThat(response.getBody().getBalance()).isEqualTo(responseDeposit.getBody().getBalance());
    }

}