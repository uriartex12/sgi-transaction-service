package com.sgi.transaction.infrastructure.controller;

import com.sgi.transaction.domain.ports.in.TransactionService;
import com.sgi.transaction.infrastructure.dto.TransactionRequest;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Controller to handle operations related to transactions.
 */
@RestController
public class TransactionController implements V1Api {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Mono<ResponseEntity<TransactionResponse>> createTransaction(Mono<TransactionRequest> transactionRequest,
                                                                       ServerWebExchange exchange) {
        return transactionService.createTransaction(transactionRequest)
                .map(transactionResponse ->
                        ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteTransaction(String id, ServerWebExchange exchange) {
        return transactionService.deleteTransaction(id)
                .map(transactionResponse -> ResponseEntity.ok().body(transactionResponse));
    }

    @Override
    public Mono<ResponseEntity<Flux<TransactionResponse>>> getAllTransactions(ServerWebExchange exchange) {
        return Mono.fromSupplier(() -> ResponseEntity.ok().body(transactionService.getAllTransactions()));
    }

    @Override
    public Mono<ResponseEntity<Flux<TransactionResponse>>> getCommissionsByProductAndPeriod(
            String productId, LocalDate startDate, LocalDate endDate, ServerWebExchange exchange) {
        return Mono.fromSupplier(() -> ResponseEntity.ok().body(transactionService
                        .getCommissionsByProductAndPeriod(productId, startDate, endDate))
        );
    }

    @Override
    public Mono<ResponseEntity<TransactionResponse>> getTransactionById(String id, ServerWebExchange exchange) {
        return transactionService.getTransactionById(id)
                .map(transactionResponse -> ResponseEntity.ok().body(transactionResponse));
    }

    @Override
    public Mono<ResponseEntity<Flux<TransactionResponse>>> getTransactionsByProductId(String productId,
                                                                                      ServerWebExchange exchange) {
        return Mono.fromSupplier(() -> ResponseEntity.ok().body(transactionService.getTransactionsByAccountId(productId)));
    }

    @Override
    public Mono<ResponseEntity<TransactionResponse>> updateTransaction(String id,
                                                                       Mono<TransactionRequest> transactionRequest,
                                                                       ServerWebExchange exchange) {
        return transactionService.updateTransaction(id, transactionRequest)
                .map(transactionResponse -> ResponseEntity.ok().body(transactionResponse));
    }
}
