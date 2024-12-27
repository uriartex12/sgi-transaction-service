package com.sgi.transaction_back.domain.ports.out;


import com.sgi.transaction_back.domain.model.Transaction;
import com.sgi.transaction_back.infrastructure.dto.TransactionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository {
    Mono<TransactionResponse> save(Transaction transaction);
    Mono<Transaction> findById(String id);
    Flux<TransactionResponse> findAll();
    Mono<Void> delete(Transaction credit);
    Flux<TransactionResponse> getTransactionsByAccountId(String accountId);

}
