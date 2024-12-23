package com.sgi.transaction_back.infrastructure.controller;

import com.sgi.transaction_back.domain.ports.in.TransactionService;
import com.sgi.transaction_back.infrastructure.dto.TransactionRequest;
import com.sgi.transaction_back.infrastructure.dto.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TransactionController implements V1Api{

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Mono<ResponseEntity<TransactionResponse>> createTransaction(Mono<TransactionRequest> transactionRequest, ServerWebExchange exchange) {
        return transactionService.createTransaction(transactionRequest);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteTransaction(String id, ServerWebExchange exchange) {
        return transactionService.deleteTransaction(id);
    }

    @Override
    public Mono<ResponseEntity<Flux<TransactionResponse>>> getAllTransactions(ServerWebExchange exchange) {
        return transactionService.getAllTransactions();
    }

    @Override
    public Mono<ResponseEntity<TransactionResponse>> getTransactionById(String id, ServerWebExchange exchange) {
        return transactionService.getTransactionById(id);
    }

    @Override
    public Mono<ResponseEntity<Flux<TransactionResponse>>> getTransactionsByAccountId(String accountId, ServerWebExchange exchange) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    @Override
    public Mono<ResponseEntity<TransactionResponse>> updateTransaction(String id, Mono<TransactionRequest> transactionRequest, ServerWebExchange exchange) {
        return transactionService.updateTransaction(id,transactionRequest);
    }
}
