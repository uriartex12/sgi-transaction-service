package com.sgi.transaction.infrastructure.controller;

import com.sgi.transaction.domain.ports.in.TransactionService;
import com.sgi.transaction.helper.FactoryTest;
import com.sgi.transaction.infrastructure.dto.AverageReportResponse;
import com.sgi.transaction.infrastructure.dto.TransactionRequest;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

/**
 * Test class for the {@link TransactionControllerTest}.
 * Utilizes {@code @WebFluxTest} to test the controller layer in isolation
 * without starting the full application context.
 */
@WebFluxTest(controllers = TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TransactionService transactionService;

    @Test
    void createTransaction_shouldReturnCreatedResponse() {
        TransactionResponse transactionResponse = FactoryTest.toFactoryTransaction(TransactionResponse.class);
        Mockito.when(transactionService.createTransaction(any(Mono.class)))
                .thenReturn(Mono.just(transactionResponse));
        webTestClient.post()
                .uri("/v1/transactions")
                .bodyValue(FactoryTest.toFactoryTransaction(TransactionRequest.class))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TransactionResponse.class)
                .consumeWith(accountResponseEntityExchangeResult -> {
                    TransactionResponse actual = accountResponseEntityExchangeResult.getResponseBody();
                    Assertions.assertNotNull(Objects.requireNonNull(actual).getClientId());
                    Assertions.assertNotNull(actual.getAmount());
                })
                .returnResult();
        Mockito.verify(transactionService, times(1)).createTransaction(any(Mono.class));
    }

    @Test
    void deleteTransaction_shouldReturnOkResponse() {
        String transactionId = randomUUID().toString();
        Mockito.when(transactionService.deleteTransaction(transactionId)).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri("/v1/transactions/{transactionId}", transactionId)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void getTransactionById_shouldReturnTransactionResponse() {
        String transactionId = UUID.randomUUID().toString();
        TransactionResponse transactionResponse = FactoryTest.toFactoryTransaction(TransactionResponse.class);
        Mockito.when(transactionService.getTransactionById(transactionId))
                .thenReturn(Mono.just(transactionResponse));
        webTestClient.get()
                .uri("/v1/transactions/{transactionId}", transactionId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(TransactionResponse.class)
                .consumeWith(System.out::println)
                .value(actual -> {
                    Assertions.assertEquals(transactionResponse.getClientId(), actual.getClientId());
                });
    }

    @Test
    void getAllTransactions_shouldReturnFluxOfTransactionResponse() {
        List<TransactionResponse> transactions =  FactoryTest.toFactoryListTransactionResponse(UUID.randomUUID().toString());
        Flux<TransactionResponse> transactionsFlux = Flux.fromIterable(transactions);
        Mockito.when(transactionService.getAllTransactions()).thenReturn(transactionsFlux);
        webTestClient.get()
                .uri("/v1/transactions")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TransactionResponse.class)
                .value(list -> assertThat(list).hasSize(1));
    }


    @Test
    void getTransactionsByProductId_shouldReturnTransactionResponse() {
        String productId = UUID.randomUUID().toString();
        TransactionResponse transactionResponse = FactoryTest.toFactoryTransaction(TransactionResponse.class);
        Mockito.when(transactionService.getTransactionsByAccountId(productId))
                .thenReturn(Flux.just(transactionResponse));
        webTestClient.get()
                .uri("/v1/transactions/{productId}/card", productId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(TransactionResponse.class);

    }

    @Test
    void getCommissionsByProductAndPeriod_shouldReturnFluxOfTransactionResponse() {
        String productId = UUID.randomUUID().toString();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        List<TransactionResponse> transactions =  FactoryTest.toFactoryListTransactionResponse(productId);
        Flux<TransactionResponse> transactionsFlux = Flux.fromIterable(transactions);
        Mockito.when(transactionService.getCommissionsByProductAndPeriod(productId, startDate, endDate)).thenReturn(transactionsFlux);
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/transactions/{productId}/commissions")
                        .queryParam("from", startDate.toString())
                        .queryParam("to", endDate.toString())
                        .build(productId))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TransactionResponse.class)
                .value(list -> assertThat(list).hasSize(1));
    }

    @Test
    void getDailyAverageBalancesForClient_shouldReturnFluxOfTransactionResponse() {
        String clientId = UUID.randomUUID().toString();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        AverageReportResponse transaction =  FactoryTest.toFactoryAverageReportResponse();
        Mockito.when(transactionService.getDailyAverageBalancesForClient(clientId, startDate, endDate)).thenReturn(Mono.just(transaction));
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/transactions/{clientId}/average-balances")
                        .queryParam("from", startDate.toString())
                        .queryParam("to", endDate.toString())
                        .build(clientId))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransactionResponse.class);
    }


    @Test
    void updateTransaction_shouldReturnTransactionResponse() {
        String transactionId = randomUUID().toString();
        TransactionRequest transactionRequest = FactoryTest.toFactoryTransaction(TransactionRequest.class);
        TransactionResponse transactionResponse = FactoryTest.toFactoryTransaction(TransactionResponse.class);

        Mockito.when(transactionService.updateTransaction(eq(transactionId), any(Mono.class)))
                .thenReturn(Mono.just(transactionResponse));

        webTestClient.put()
                .uri("/v1/transactions/{transactionId}", transactionId)
                .bodyValue(transactionRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(TransactionResponse.class);

        Mockito.verify(transactionService, times(1)).updateTransaction(eq(transactionId), any(Mono.class));
    }



}
