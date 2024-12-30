package com.sgi.transaction.infrastructure.mapper;

import com.sgi.transaction.domain.model.Transaction;
import com.sgi.transaction.infrastructure.dto.TransactionRequest;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;

/**
 * Mapper interface for converting between Transaction domain objects and DTOs.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper
public interface TransactionMapper {

    /**
     * Singleton instance for the TransactionMapper.
     */
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    /**
     * Maps a Transaction domain object to a TransactionResponse DTO.
     *
     * @param transaction the Transaction object to map
     * @return the mapped TransactionResponse
     */
    @Mapping(target = "balance", source = "balance", defaultValue = "0.0")
    TransactionResponse map(Transaction transaction);

    /**
     * Maps a TransactionRequest DTO to a Transaction domain object.
     *
     * @param customer the TransactionRequest to map
     * @return the mapped Transaction domain object
     */
    @Mapping(target = "id", ignore = true)
    Transaction map(TransactionRequest customer);

    /**
     * Maps a Mono of TransactionRequest to a Mono of Transaction domain object.
     *
     * @param transactionRequestMono the Mono containing the TransactionRequest
     * @return a Mono containing the mapped Transaction domain object
     */
    default Mono<Transaction> map(Mono<TransactionRequest> transactionRequestMono) {
        return transactionRequestMono.map(this::created);
    }

    /**
     * Creates a Transaction domain object from a TransactionRequest DTO.
     * Includes default values for type, createdDate, and updatedDate fields.
     *
     * @param transaction the TransactionRequest to map
     * @return the created Transaction domain object
     */
    @Mapping(target = "type", source = "type")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "updatedDate", expression = "java(java.time.Instant.now())")
    Transaction created(TransactionRequest transaction);
}
