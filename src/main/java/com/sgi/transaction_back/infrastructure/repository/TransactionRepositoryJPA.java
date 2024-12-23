package com.sgi.transaction_back.infrastructure.repository;

import com.sgi.transaction_back.domain.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepositoryJPA extends ReactiveMongoRepository<Transaction,String> {

    Flux<Transaction> findAllByBankAccountId(String bankAccountId);
}
