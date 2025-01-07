package com.sgi.transaction.infrastructure.repository;

import com.sgi.transaction.domain.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

/**
 * Repository interface for managing {@link Transaction} entities.
 * Extends {@link ReactiveMongoRepository} to provide reactive database operations.
 */
public interface TransactionRepositoryJpa extends ReactiveMongoRepository<Transaction, String> {

    Flux<Transaction> findAllByProductId(String bankAccountId);

    @Query("{ 'productId': ?0, 'commission': { $ne: null, $gt: 0 }, 'createdDate': { $gte: ?1, $lte: ?2 } }")
    Flux<Transaction> findCommissionsByProductAndDateRange(String productId, LocalDate startDate, LocalDate endDate);

    Flux<Transaction> findByClientIdAndCreatedDateBetween(String clientId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT t FROM Transaction t WHERE (:productId IS NULL OR t.productId = :productId) AND (:cardId IS NULL OR t.cardId = :cardId)")
    Flux<Transaction> findByProductIdOrCardId(@Param("productId") String productId,
                                              @Param("cardId") String cardId,
                                              Pageable pageable);

}
