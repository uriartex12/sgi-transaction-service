package com.sgi.transaction.domain.ports.in;

import com.sgi.transaction.infrastructure.dto.TransactionRequest;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface that defines the operations for managing transactions.
 * Provides methods to create, update, delete, and retrieve transactions.
 */
public interface TransactionService {

    /**
     * Creates a new transaction.
     *
     * @param customer the transaction request containing the details of the transaction to be created
     * @return a Mono emitting the created transaction response
     */
    Mono<TransactionResponse> createTransaction(Mono<TransactionRequest> customer);

    /**
     * Deletes a transaction by its ID.
     *
     * @param id the ID of the transaction to delete
     * @return a Mono that completes upon successful deletion
     */
    Mono<Void> deleteTransaction(String id);

    /**
     * Retrieves all transactions.
     *
     * @return a Flux emitting all transaction responses
     */
    Flux<TransactionResponse> getAllTransactions();

    /**
     * Retrieves a transaction by its ID.
     *
     * @param id the ID of the transaction to retrieve
     * @return a Mono emitting the transaction response, or empty if not found
     */
    Mono<TransactionResponse> getTransactionById(String id);

    /**
     * Updates an existing transaction by its ID.
     *
     * @param id       the ID of the transaction to update
     * @param customer the updated transaction request data
     * @return a Mono emitting the updated transaction response
     */
    Mono<TransactionResponse> updateTransaction(String id, Mono<TransactionRequest> customer);

    /**
     * Retrieves transactions by account ID.
     *
     * @param accountId the ID of the account
     * @return a Flux emitting transaction responses associated with the account ID
     */
    Flux<TransactionResponse> getTransactionsByAccountId(String accountId);
}
