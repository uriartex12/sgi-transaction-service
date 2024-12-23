package com.sgi.transaction_back.infrastructure.repository.impl;

import com.sgi.transaction_back.domain.model.Transaction;
import com.sgi.transaction_back.domain.ports.out.TransactionRepository;
import com.sgi.transaction_back.infrastructure.dto.TransactionRequest;
import com.sgi.transaction_back.infrastructure.dto.TransactionResponse;
import com.sgi.transaction_back.infrastructure.mapper.TransactionMapper;
import com.sgi.transaction_back.infrastructure.repository.TransactionRepositoryJPA;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionRepositoryJPA transactionRepository;

    public TransactionRepositoryImpl(TransactionRepositoryJPA transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Mono<TransactionResponse> createTransaction(Mono<TransactionRequest> transaction) {
        return transaction.flatMap(custom ->
                    TransactionMapper.INSTANCE.map(Mono.just(custom))
                            .flatMap(transactionRepository::save)
            ).map(TransactionMapper.INSTANCE::map);
    }

    @Override
    public Mono<Void> deleteTransaction(String id) {
        return transactionRepository.findById(id)
                .flatMap(transactionRepository::delete)
                .switchIfEmpty(Mono.error(new Exception("Transaction not found")));
    }

    @Override
    public Flux<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .map(TransactionMapper.INSTANCE::map);
    }

    @Override
    public Mono<TransactionResponse> getTransactionById(String id) {
        return transactionRepository.findById(id)
                .map(TransactionMapper.INSTANCE::map);
    }

    @Override
    public Mono<TransactionResponse> updateTransaction(String id, Mono<TransactionRequest> customer) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new Exception("Transaction not found")))
                .flatMap(transaction ->
                        customer.map(updatedTransaction -> {
                            Transaction updatedEntity = TransactionMapper.INSTANCE.map(updatedTransaction,transaction.getId());
                            updatedEntity.setId(transaction.getId());
                            updatedEntity.setUpdatedDate(Instant.now());
                            return updatedEntity;
                        })
                )
                .flatMap(transactionRepository::save)
                .map(TransactionMapper.INSTANCE::map);
    }

    @Override
    public Flux<TransactionResponse> getTransactionsByAccountId(String accountId) {
        return transactionRepository.findAllByBankAccountId(accountId)
                .map(TransactionMapper.INSTANCE::map);
    }
}