package com.sgi.transaction.infrastructure.repository.impl;

import com.sgi.transaction.domain.model.Transaction;
import com.sgi.transaction.domain.ports.out.TransactionRepository;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import com.sgi.transaction.infrastructure.mapper.TransactionMapper;
import com.sgi.transaction.infrastructure.repository.TransactionRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .map(TransactionMapper.INSTANCE::toTransactionResponse);
    }

    @Override
    public Mono<Transaction> findById(String id) {
        return repositoryJpa.findById(id);
    }

    @Override
    public Flux<TransactionResponse> findAll(String productId, String cardId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size);
        if (productId == null && cardId == null) {
            return repositoryJpa.findAll()
                    .map(TransactionMapper.INSTANCE::toTransactionResponse);
        } else {
            return repositoryJpa.findByProductIdOrCardId(productId, cardId, pageable)
                    .map(TransactionMapper.INSTANCE::toTransactionResponse);
        }
    }

    @Override
    public Mono<Void> delete(Transaction credit) {
        return repositoryJpa.delete(credit);
    }

    @Override
    public Flux<TransactionResponse> getTransactionsByAccountId(String accountId) {
        return repositoryJpa.findAllByProductId(accountId)
                .map(TransactionMapper.INSTANCE::toTransactionResponse);
    }

    @Override
    public Flux<TransactionResponse> getCommissionsByProductAndPeriod(String productId, LocalDate startDate, LocalDate endDate) {
        return repositoryJpa.findCommissionsByProductAndDateRange(productId, startDate, endDate)
                .map(TransactionMapper.INSTANCE::toTransactionResponse);
    }

    @Override
    public Flux<TransactionResponse> getDailyAverageBalancesForClient(String clientId, LocalDate startDate, LocalDate endDate) {
        return repositoryJpa.findByClientIdAndCreatedDateBetween(clientId, startDate, endDate)
                .map(TransactionMapper.INSTANCE::toTransactionResponse);
    }
}