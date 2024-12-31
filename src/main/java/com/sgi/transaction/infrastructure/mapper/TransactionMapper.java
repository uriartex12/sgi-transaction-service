package com.sgi.transaction.infrastructure.mapper;

import com.sgi.transaction.domain.model.Transaction;
import com.sgi.transaction.infrastructure.dto.TransactionRequest;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Mapper interface for converting between Transaction domain objects and DTOs.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "balance", source = "balance", defaultValue = "0.0")
    TransactionResponse toTransactionResponse(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    Transaction toTransaction(TransactionRequest customer);

    @Mapping(target = "type", source = "type")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "updatedDate", expression = "java(java.time.Instant.now())")
    Transaction created(TransactionRequest transaction);

    default Mono<Transaction> map(Mono<TransactionRequest> transactionRequestMono) {
        return transactionRequestMono.map(this::created);
    }

    default OffsetDateTime map(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null;
    }
}
