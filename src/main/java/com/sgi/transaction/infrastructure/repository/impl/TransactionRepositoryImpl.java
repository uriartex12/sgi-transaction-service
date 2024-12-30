package com.sgi.transaction.infrastructure.repository.impl;

import com.sgi.transaction.domain.model.Transaction;
import com.sgi.transaction.domain.ports.out.TransactionRepository;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import com.sgi.transaction.infrastructure.mapper.TransactionMapper;
import com.sgi.transaction.infrastructure.repository.TransactionRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Implementation of {@link TransactionRepository} that uses {@link TransactionRepositoryJpa}
 * for database interactions. Provides reactive methods to manage transactions.
 */
@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionRepositoryJpa repositoryJpa;

    @Override
    public Mono<TransactionResponse> save(Transaction transaction) {
        return repositoryJpa.save(transaction)
                .map(TransactionMapper.INSTANCE::map);
    }

    @Override
    public Mono<Transaction> findById(String id) {
        return repositoryJpa.findById(id);
    }

    @Override
    public Flux<TransactionResponse> findAll() {
        return repositoryJpa.findAll()
                .map(TransactionMapper.INSTANCE::map);
    }

    @Override
    public Mono<Void> delete(Transaction credit) {
        return repositoryJpa.delete(credit);
    }

    @Override
    public Flux<TransactionResponse> getTransactionsByAccountId(String accountId) {
        return repositoryJpa.findAllByProductId(accountId)
                .map(TransactionMapper.INSTANCE::map);
    }

    @Override
    public Flux<TransactionResponse> getCommissionsByProductAndPeriod(String productId, LocalDate startDate, LocalDate endDate) {
        return repositoryJpa.findCommissionsByProductAndDateRange(productId, startDate, endDate);
    }
}