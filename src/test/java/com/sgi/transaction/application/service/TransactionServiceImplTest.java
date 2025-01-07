package com.sgi.transaction.application.service;

import com.sgi.transaction.domain.model.Transaction;
import com.sgi.transaction.domain.ports.out.TransactionRepository;
import com.sgi.transaction.helper.FactoryTest;
import com.sgi.transaction.infrastructure.dto.AverageReportResponse;
import com.sgi.transaction.infrastructure.dto.TransactionRequest;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import com.sgi.transaction.infrastructure.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

/**
 * Unit test class for the TransactionServiceImpl service.
 * This class contains unit tests for the methods of the TransactionServiceImpl class,
 * ensuring that transactions are managed correctly, data is validated,
 * and potential errors are handled appropriately.
 */
@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private  TransactionServiceImpl transactionService;


    @Test
    void createTransaction_shouldReturnCreatedResponse() {
        TransactionRequest transactionRequest = FactoryTest.toFactoryTransaction(TransactionRequest.class);
        TransactionResponse transactionResponse = FactoryTest.toFactoryTransaction(TransactionResponse.class);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(Mono.just(transactionResponse));

        Mono<TransactionResponse> result = transactionService.createTransaction(Mono.just(transactionRequest));

        StepVerifier.create(result)
                .expectNext(transactionResponse)
                .verifyComplete();

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void deleteTransaction_shouldReturnVoid() {
        String transactionId = UUID.randomUUID().toString();
        Transaction transaction = FactoryTest.toFactoryEntityTransaction();
        transaction.setId(transactionId);
        when(transactionRepository.findById(transactionId)).thenReturn(Mono.just(transaction));
        when(transactionRepository.delete(transaction)).thenReturn(Mono.empty());
        Mono<Void> result = transactionService.deleteTransaction(transactionId);
        StepVerifier.create(result)
                .verifyComplete();
        verify(transactionRepository).findById(transactionId);
        verify(transactionRepository).delete(transaction);
    }

    @Test
    void deleteTransaction_shouldReturnNotFound() {
        String transactionId = UUID.randomUUID().toString();
        when(transactionRepository.findById(transactionId)).thenReturn(Mono.empty());
        Mono<Void> result = transactionService.deleteTransaction(transactionId);
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof CustomException
                        &&
                        throwable.getMessage().equals("Transaction not found"))
                .verify();
        verify(transactionRepository).findById(transactionId);
        verify(transactionRepository, never()).delete(any());
    }

    @Test
    void getAllTransactions_shouldReturnListTransactionResponse() {
        List<TransactionResponse> transactions = FactoryTest.toFactoryListTransactionResponse(UUID.randomUUID().toString());
        when(transactionRepository.findAll(anyString(), anyString(), anyInt(), anyInt())).thenReturn(Flux.fromIterable(transactions));
        Flux<TransactionResponse> result = transactionService.getAllTransactions(anyString(), anyString(), anyInt(), anyInt());

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
        verify(transactionRepository).findAll(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void getTransactionById_shouldReturnListTransactionResponse() {
        String transactionId =  UUID.randomUUID().toString();
        Transaction transaction = FactoryTest.toFactoryEntityTransaction();
        when(transactionRepository.findById(transactionId)).thenReturn(Mono.just(transaction));
        Mono<TransactionResponse> result = transactionService.getTransactionById(transactionId);
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    void getTransactionsByAccountId_shouldReturnListTransactionResponse() {
        String accountId =  UUID.randomUUID().toString();
        List<TransactionResponse> transaction = FactoryTest.toFactoryListTransactionResponse(accountId);
        when(transactionRepository.getTransactionsByAccountId(accountId)).thenReturn(Flux.fromIterable(transaction));
        Flux<TransactionResponse> result = transactionService.getTransactionsByAccountId(accountId);
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
        verify(transactionRepository).getTransactionsByAccountId(accountId);
    }

    @Test
    void getCommissionsByProductAndPeriod_shouldReturnListTransactionResponse() {
        String productId = UUID.randomUUID().toString();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        List<TransactionResponse> transaction = FactoryTest.toFactoryListTransactionResponse(productId);
        when(transactionRepository.getCommissionsByProductAndPeriod(productId, startDate, endDate)).thenReturn(Flux.fromIterable(transaction));
        Flux<TransactionResponse> result = transactionService.getCommissionsByProductAndPeriod(productId, startDate, endDate);
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
        verify(transactionRepository).getCommissionsByProductAndPeriod(productId, startDate, endDate);
    }

    @Test
    void getDailyAverageBalancesForClient_shouldReturnListTransactionResponse() {
        String clientId = UUID.randomUUID().toString();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        List<TransactionResponse> transaction = FactoryTest.toFactoryListTransactionResponse(clientId);
        when(transactionRepository.getDailyAverageBalancesForClient(clientId, startDate, endDate)).thenReturn(Flux.fromIterable(transaction));
        Mono<AverageReportResponse> result = transactionService.getDailyAverageBalancesForClient(clientId, startDate, endDate);
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getClientId().equals(clientId))
                .verifyComplete();
    }




}
