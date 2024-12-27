package com.sgi.transaction_back.infrastructure.repository.impl;

import com.sgi.transaction_back.domain.model.Transaction;
import com.sgi.transaction_back.domain.ports.out.TransactionRepository;
import com.sgi.transaction_back.infrastructure.dto.TransactionResponse;
import com.sgi.transaction_back.infrastructure.mapper.TransactionMapper;
import com.sgi.transaction_back.infrastructure.repository.TransactionRepositoryJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionRepositoryJPA repositoryJPA;

    @Override
    public Mono<TransactionResponse> save(Transaction transaction) {
        return repositoryJPA.save(transaction)
                .map(TransactionMapper.INSTANCE::map);
    }

    @Override
    public Mono<Transaction> findById(String id) {
        return repositoryJPA.findById(id);
    }

    @Override
    public Flux<TransactionResponse> findAll() {
        return repositoryJPA.findAll()
                .map(TransactionMapper.INSTANCE::map);
    }

    @Override
    public Mono<Void> delete(Transaction credit) {
        return repositoryJPA.delete(credit);
    }

    @Override
    public Flux<TransactionResponse> getTransactionsByAccountId(String accountId) {
        return repositoryJPA.findAllByProductId(accountId)
                .map(TransactionMapper.INSTANCE::map);
    }
}