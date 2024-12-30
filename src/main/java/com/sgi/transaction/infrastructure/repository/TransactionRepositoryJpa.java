package com.sgi.transaction.infrastructure.repository;

import com.sgi.transaction.domain.model.Transaction;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

/**
 * Repository interface for managing {@link Transaction} entities.
 * Extends {@link ReactiveMongoRepository} to provide reactive database operations.
 */
public interface TransactionRepositoryJpa extends ReactiveMongoRepository<Transaction, String> {

    Flux<Transaction> findAllByProductId(String bankAccountId);

    @Query("{ 'productId': ?0, 'commission': { $ne: null, $gt: 0 }, 'createdDate': { $gte: ?1, $lte: ?2 } }")
    Flux<TransactionResponse> findCommissionsByProductAndDateRange(String productId, LocalDate startDate, LocalDate endDate);

}
