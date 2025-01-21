package com.sgi.transaction.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Represents a transaction entity in the system.
 * This entity is mapped to the "transaction" collection in the database.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transaction")
@CompoundIndexes({
        @CompoundIndex(name = "product_card_idx", def = "{'productId' : 1, 'cardId' : 1}"),
        @CompoundIndex(name = "created_date_idx", def = "{'createdDate' : -1}")
})
public class Transaction {
    @Id
    private String id;
    private String productId;
    private String type;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal commission;
    private String description;
    private String clientId;
    private String destinationProductId;
    private String cardId;
    private String currency;
    private String paymentMethod;
    private String operation;
    private String status;
    private String bootcoinId;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal buyRate;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal sellRate;
    private String walletId;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal balance;
    private Object sender;
    private Object receiver;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedDate
    private Instant updatedDate;
}