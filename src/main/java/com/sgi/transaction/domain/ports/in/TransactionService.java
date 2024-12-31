package com.sgi.transaction.domain.ports.in;

import com.sgi.transaction.infrastructure.dto.AverageReportResponse;
import com.sgi.transaction.infrastructure.dto.TransactionRequest;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Service interface that defines the operations for managing transactions.
 * Provides methods to create, update, delete, and retrieve transactions.
 */
public interface TransactionService {

    Mono<TransactionResponse> createTransaction(Mono<TransactionRequest> customer);

    Mono<Void> deleteTransaction(String id);

    Flux<TransactionResponse> getAllTransactions();

    Mono<TransactionResponse> getTransactionById(String id);

    Mono<TransactionResponse> updateTransaction(String id, Mono<TransactionRequest> customer);

    Flux<TransactionResponse> getTransactionsByAccountId(String accountId);

    Flux<TransactionResponse> getCommissionsByProductAndPeriod(String productId, LocalDate startDate, LocalDate endDate);

    Mono<AverageReportResponse> getDailyAverageBalancesForClient(String clientId, LocalDate startDate, LocalDate endDate);
}


