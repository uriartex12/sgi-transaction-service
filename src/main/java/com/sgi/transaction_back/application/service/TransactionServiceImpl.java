package com.sgi.transaction_back.application.service;

import com.sgi.transaction_back.domain.ports.in.TransactionService;
import com.sgi.transaction_back.domain.ports.out.TransactionRepository;
import com.sgi.transaction_back.infrastructure.dto.TransactionRequest;
import com.sgi.transaction_back.infrastructure.dto.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Mono<ResponseEntity<TransactionResponse>> createTransaction(Mono<TransactionRequest> transaction) {
        return transactionRepository.createTransaction(transaction)
                .map(createdCustomer ->ResponseEntity.ok().body(createdCustomer))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteTransaction(String id) {
        return transactionRepository.deleteTransaction(id)
                .map(updateCustomer-> ResponseEntity.ok().body(updateCustomer))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<TransactionResponse>>> getAllTransactions() {
        return Mono.fromSupplier(() -> ResponseEntity.ok().body(transactionRepository.getAllTransactions()))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<TransactionResponse>> getTransactionById(String id) {
        return transactionRepository.getTransactionById(id)
                .map(createdCustomer ->ResponseEntity.ok().body(createdCustomer))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<TransactionResponse>> updateTransaction(String id, Mono<TransactionRequest> transaction) {
        return transactionRepository.updateTransaction(id, transaction)
                .map(createdCustomer ->ResponseEntity.ok().body(createdCustomer))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<TransactionResponse>>> getTransactionsByAccountId(String accountId) {
        return Mono.fromSupplier(() -> ResponseEntity.ok().body(transactionRepository.getTransactionsByAccountId(accountId)))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }
}
