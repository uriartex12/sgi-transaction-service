package com.sgi.transaction.application.service;

import com.sgi.transaction.domain.model.Transaction;
import com.sgi.transaction.domain.ports.in.TransactionService;
import com.sgi.transaction.domain.ports.out.TransactionRepository;
import com.sgi.transaction.domain.shared.CustomError;
import com.sgi.transaction.infrastructure.dto.AverageReportResponse;
import com.sgi.transaction.infrastructure.dto.Product;
import com.sgi.transaction.infrastructure.dto.DailyAverages;
import com.sgi.transaction.infrastructure.dto.TransactionRequest;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import com.sgi.transaction.infrastructure.exception.CustomException;
import com.sgi.transaction.infrastructure.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Mono<TransactionResponse> createTransaction(Mono<TransactionRequest> transactionRequestMono) {
        return transactionRequestMono.flatMap(transactionRequest -> {
            transactionRequest.setCommission(Objects.requireNonNullElse(transactionRequest.getCommission(), 0d));
            transactionRequest.setStatus(TransactionRequest.StatusEnum.COMPLETED);
            return TransactionMapper.INSTANCE.map(Mono.just(transactionRequest))
                    .flatMap(transactionRepository::save);
        });
    }

    @Override
    public Mono<Void> deleteTransaction(String id) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(CustomError.E_TRANSACTION_NOT_FOUND)))
                .flatMap(transactionRepository::delete);
    }

    @Override
    public Flux<TransactionResponse> getAllTransactions(String productId, String cardId, Integer page, Integer size) {
        return transactionRepository.findAll(productId, cardId, page, size);
    }

    @Override
    public Mono<TransactionResponse> getTransactionById(String id) {
        return transactionRepository.findById(id)
                .map(TransactionMapper.INSTANCE::toTransactionResponse);
    }

    @Override
    public Mono<TransactionResponse> updateTransaction(String id, Mono<TransactionRequest> transactionRequestMono) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(CustomError.E_TRANSACTION_NOT_FOUND)))
                .flatMap(transaction ->
                        transactionRequestMono.map(transactionRequest -> {
                            Transaction updatedTransaction = TransactionMapper.INSTANCE.toTransaction(transactionRequest);
                            updatedTransaction.setId(transaction.getId());
                            updatedTransaction.setStatus(Optional.ofNullable(transactionRequest.getStatus())
                                    .map(Enum::name)
                                    .orElse(transaction.getStatus()));
                            updatedTransaction.setUpdatedDate(Instant.now());
                            return updatedTransaction;
                        })
                )
                .flatMap(transactionRepository::save);
    }

    @Override
    public Flux<TransactionResponse> getTransactionsByAccountId(String accountId) {
        return transactionRepository.getTransactionsByAccountId(accountId);
    }

    @Override
    public Flux<TransactionResponse> getCommissionsByProductAndPeriod(String productId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.getCommissionsByProductAndPeriod(productId, startDate, endDate);
    }

    @Override
    public Mono<AverageReportResponse> getDailyAverageBalancesForClient(String clientId, LocalDate startDate, LocalDate endDate) {
        Flux<TransactionResponse> transactions = transactionRepository.getDailyAverageBalancesForClient(clientId, startDate, endDate);
        return transactions
                .groupBy(TransactionResponse::getProductId)
                .flatMap(productGroup ->
                        productGroup
                                .collectList()
                                .map(transactionsList -> {
                                    List<DailyAverages> dailyAverages = calculateDailyAverages(transactionsList);
                                    return new Product(
                                            productGroup.key(),
                                            dailyAverages
                                    );
                                })
                )
                .collectList()
                .map(productAverages -> new AverageReportResponse(clientId, productAverages));
    }

    private List<DailyAverages> calculateDailyAverages(List<TransactionResponse> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCreatedDate().toLocalDate(),
                        Collectors.averagingDouble(TransactionResponse::getBalance)
                ))
                .entrySet()
                .stream()
                .map(entry -> new DailyAverages(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

}
