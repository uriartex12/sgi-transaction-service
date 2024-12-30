package com.sgi.transaction.domain.ports.out;

import com.sgi.transaction.domain.model.Transaction;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Interface that defines the contract for transaction repository operations.
 * This repository handles operations related to transactions, such as saving,
 * finding, deleting, and retrieving transactions by account ID.
 */
public interface TransactionRepository {

    Mono<TransactionResponse> save(Transaction transaction);

    Mono<Transaction> findById(String id);

    Flux<TransactionResponse> findAll();

    Mono<Void> delete(Transaction transaction);

    Flux<TransactionResponse> getTransactionsByAccountId(String accountId);

    Flux<TransactionResponse> getCommissionsByProductAndPeriod(String productId, LocalDate startDate, LocalDate endDate);

}
