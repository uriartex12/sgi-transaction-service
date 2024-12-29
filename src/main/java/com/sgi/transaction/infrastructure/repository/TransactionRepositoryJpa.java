package com.sgi.transaction.infrastructure.repository;

import com.sgi.transaction.domain.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * Repository interface for managing {@link Transaction} entities.
 * Extends {@link ReactiveMongoRepository} to provide reactive database operations.
 */
public interface TransactionRepositoryJpa extends ReactiveMongoRepository<Transaction, String> {

    Flux<Transaction> findAllByProductId(String bankAccountId);
}
