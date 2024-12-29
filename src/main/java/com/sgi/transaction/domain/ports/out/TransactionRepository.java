package com.sgi.transaction.domain.ports.out;

import com.sgi.transaction.domain.model.Transaction;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface that defines the contract for transaction repository operations.
 * This repository handles operations related to transactions, such as saving,
 * finding, deleting, and retrieving transactions by account ID.
 */
public interface TransactionRepository {

    /**
     * Saves a transaction and returns the corresponding response.
     *
     * @param transaction the transaction to save
     * @return a Mono emitting the transaction response upon completion
     */
    Mono<TransactionResponse> save(Transaction transaction);

    /**
     * Finds a transaction by its ID.
     *
     * @param id the ID of the transaction to find
     * @return a Mono emitting the found transaction, or empty if not found
     */
    Mono<Transaction> findById(String id);

    /**
     * Retrieves all transactions.
     *
     * @return a Flux emitting all transaction responses
     */
    Flux<TransactionResponse> findAll();

    /**
     * Deletes a specified transaction.
     *
     * @param transaction the transaction to delete
     * @return a Mono that completes upon successful deletion
     */
    Mono<Void> delete(Transaction transaction);

    /**
     * Retrieves all transactions associated with a specific account ID.
     *
     * @param accountId the ID of the account
     * @return a Flux emitting transaction responses related to the account ID
     */
    Flux<TransactionResponse> getTransactionsByAccountId(String accountId);
}
