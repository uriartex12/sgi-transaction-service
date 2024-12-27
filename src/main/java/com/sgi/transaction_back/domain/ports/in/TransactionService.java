package com.sgi.transaction_back.domain.ports.in;

import com.sgi.transaction_back.infrastructure.dto.TransactionRequest;
import com.sgi.transaction_back.infrastructure.dto.TransactionResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<TransactionResponse> createTransaction(Mono<TransactionRequest> customer);
    Mono<Void> deleteTransaction(String id);
    Flux<TransactionResponse> getAllTransactions();
    Mono<TransactionResponse> getTransactionById(String id);
    Mono<TransactionResponse> updateTransaction(String id, Mono<TransactionRequest> customer);
    Flux<TransactionResponse> getTransactionsByAccountId(String accountId);
}
