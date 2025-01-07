package com.sgi.transaction.infrastructure.repository;

import com.sgi.transaction.domain.model.Transaction;
import com.sgi.transaction.helper.FactoryTest;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import com.sgi.transaction.infrastructure.mapper.TransactionMapper;
import com.sgi.transaction.infrastructure.repository.impl.TransactionRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * Unit test class for the {@link TransactionRepositoryImpl} repository.
 * This class contains unit tests for the methods in {@link TransactionRepositoryImpl},
 * ensuring correct interaction with the data layer, such as retrieving and saving transactions.
 * Tests typically mock dependencies to isolate the repository's behavior.
 */
@ExtendWith(MockitoExtension.class)
public class TransactionRepositoryImplTest {

    @InjectMocks
    private TransactionRepositoryImpl transactionRepository;

    @Mock
    private TransactionRepositoryJpa repositoryJpa;

    @Test
    public void testSave() {
        Transaction transaction = FactoryTest.toFactoryEntityTransaction();
        TransactionResponse transactionResponse = TransactionMapper.INSTANCE.toTransactionResponse(transaction);
        when(repositoryJpa.save(transaction)).thenReturn(Mono.just(transaction));
        Mono<TransactionResponse> result = transactionRepository.save(transaction);
        StepVerifier.create(result)
                .expectNext(transactionResponse)
                .verifyComplete();

        verify(repositoryJpa, times(1)).save(transaction);
    }

    @Test
    public void testFindById() {
        String transactionId = UUID.randomUUID().toString();
        Transaction transaction = FactoryTest.toFactoryEntityTransaction();
        when(repositoryJpa.findById(transactionId))
                .thenReturn(Mono.just(transaction));
        Mono<Transaction> result = transactionRepository.findById(transactionId);
        StepVerifier.create(result)
                .expectNext(transaction)
                .verifyComplete();

        verify(repositoryJpa, times(1)).findById(transactionId);
    }

    @Test
    public void testFindAll() {
        String productId = UUID.randomUUID().toString();
        String cardId = UUID.randomUUID().toString();
        Pageable pageable = PageRequest.of(1, 10, Sort.by("createdDate").descending());
        Transaction transaction1 = FactoryTest.toFactoryEntityTransaction();
        Transaction transaction2 = FactoryTest.toFactoryEntityTransaction();

        when(repositoryJpa.findByProductIdOrCardId(productId, cardId, pageable))
                .thenReturn(Flux.just(transaction1, transaction2));
        Flux<TransactionResponse> result = transactionRepository.findAll(productId, cardId, 1, 10);
        result.collectList().subscribe(responses -> {
            assertNotNull(responses);
            assertEquals(2, responses.size());
        });
        verify(repositoryJpa, times(1)).findByProductIdOrCardId(productId, cardId, pageable);
    }



    @Test
    public void testDelete() {
        Transaction transaction = FactoryTest.toFactoryEntityTransaction();
        transaction.setId(UUID.randomUUID().toString());
        when(repositoryJpa.delete(transaction)).thenReturn(Mono.empty());
        Mono<Void> result = transactionRepository.delete(transaction);
        StepVerifier.create(result)
                .verifyComplete();
        verify(repositoryJpa, times(1)).delete(transaction);
    }

    @Test
    public void testGetTransactionsByAccountId() {
        String productId = UUID.randomUUID().toString();
        Transaction transaction = FactoryTest.toFactoryEntityTransaction();
        when(repositoryJpa.findAllByProductId(productId)).thenReturn(Flux.just(transaction));
        Flux<TransactionResponse> result = transactionRepository.getTransactionsByAccountId(productId);
        StepVerifier.create(result)
                .expectNext(TransactionMapper.INSTANCE.toTransactionResponse(transaction))
                .verifyComplete();
        verify(repositoryJpa, times(1)).findAllByProductId(productId);
    }

    @Test
    public void testGetCommissionsByProductAndPeriod() {
        String productId = UUID.randomUUID().toString();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        Transaction transaction = FactoryTest.toFactoryEntityTransaction();
        when(repositoryJpa.findCommissionsByProductAndDateRange(productId, startDate, endDate)).thenReturn(Flux.just(transaction));
        Flux<TransactionResponse> result = transactionRepository.getCommissionsByProductAndPeriod(productId, startDate, endDate);
        StepVerifier.create(result)
                .expectNext(TransactionMapper.INSTANCE.toTransactionResponse(transaction))
                .verifyComplete();
        verify(repositoryJpa, times(1)).findCommissionsByProductAndDateRange(productId, startDate, endDate);
    }


    @Test
    public void  testGetDailyAverageBalancesForClient() {
        String clientId = UUID.randomUUID().toString();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        Transaction transaction = FactoryTest.toFactoryEntityTransaction();
        when(repositoryJpa.findByClientIdAndCreatedDateBetween(clientId, startDate, endDate)).thenReturn(Flux.just(transaction));
        Flux<TransactionResponse> result = transactionRepository.getDailyAverageBalancesForClient(clientId, startDate, endDate);
        StepVerifier.create(result)
                .expectNext(TransactionMapper.INSTANCE.toTransactionResponse(transaction))
                .verifyComplete();
        verify(repositoryJpa, times(1)).findByClientIdAndCreatedDateBetween(clientId, startDate, endDate);
    }

}
