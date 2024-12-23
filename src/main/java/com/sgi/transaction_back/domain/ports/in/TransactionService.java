package com.sgi.transaction_back.domain.ports.in;

import com.sgi.transaction_back.infrastructure.dto.TransactionRequest;
import com.sgi.transaction_back.infrastructure.dto.TransactionResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<ResponseEntity<TransactionResponse>> createTransaction(Mono<TransactionRequest> customer);
    Mono<ResponseEntity<Void>> deleteTransaction(String id);
    Mono<ResponseEntity<Flux<TransactionResponse>>> getAllTransactions();
    Mono<ResponseEntity<TransactionResponse>> getTransactionById(String id);
    Mono<ResponseEntity<TransactionResponse>> updateTransaction(String id, Mono<TransactionRequest> customer);
    Mono<ResponseEntity<Flux<TransactionResponse>>> getTransactionsByAccountId(String accountId);
}
