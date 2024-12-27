package com.sgi.transaction_back.infrastructure.mapper;

import com.sgi.transaction_back.domain.model.Transaction;
import com.sgi.transaction_back.infrastructure.dto.TransactionRequest;
import com.sgi.transaction_back.infrastructure.dto.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "description", source = "description",defaultValue = "No description")
    @Mapping(target = "balance", source = "balance",defaultValue = "0.0")
    TransactionResponse map(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    Transaction map(TransactionRequest customer);

    @Mapping(target = "type", source = "type")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "updatedDate", expression = "java(java.time.Instant.now())")
    Transaction created(TransactionRequest transaction);

    default Mono<Transaction> map(Mono<TransactionRequest> transactionRequestMono) {
        return transactionRequestMono.map(this::created);
    }
}

