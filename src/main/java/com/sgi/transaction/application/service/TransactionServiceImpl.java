package com.sgi.transaction.application.service;

import com.sgi.transaction.domain.model.Transaction;
import com.sgi.transaction.domain.ports.in.TransactionService;
import com.sgi.transaction.domain.ports.out.TransactionRepository;
import com.sgi.transaction.domain.shared.CustomError;
import com.sgi.transaction.infrastructure.dto.TransactionRequest;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import com.sgi.transaction.infrastructure.exception.CustomException;
import com.sgi.transaction.infrastructure.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Implementation of the {@link TransactionService} interface.
 * Provides operations for managing transactions such as creating, updating, deleting,
 * and retrieving transactions.
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Mono<TransactionResponse> createTransaction(Mono<TransactionRequest> transaction) {
        return transaction.flatMap(custom ->
                TransactionMapper.INSTANCE.map(Mono.just(custom))
                        .flatMap(transactionRepository::save));
    }

    @Override
    public Mono<Void> deleteTransaction(String id) {
        return transactionRepository.findById(id)
                .flatMap(transactionRepository::delete)
                .switchIfEmpty(Mono.error(new CustomException(CustomError.E_TRANSACTION_NOT_FOUND)));
    }

    @Override
    public Flux<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Mono<TransactionResponse> getTransactionById(String id) {
        return transactionRepository.findById(id)
                .map(TransactionMapper.INSTANCE::map);
    }

    @Override
    public Mono<TransactionResponse> updateTransaction(String id, Mono<TransactionRequest> customer) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(CustomError.E_TRANSACTION_NOT_FOUND)))
                .flatMap(transaction ->
                        customer.map(updatedTransaction -> {
                            Transaction updatedEntity = TransactionMapper.INSTANCE.map(updatedTransaction);
                            updatedEntity.setId(transaction.getId());
                            updatedEntity.setUpdatedDate(Instant.now());
                            return updatedEntity;
                        })
                )
                .flatMap(transactionRepository::save);
    }

    @Override
    public Flux<TransactionResponse> getTransactionsByAccountId(String accountId) {
        return transactionRepository.getTransactionsByAccountId(accountId);
    }
}
