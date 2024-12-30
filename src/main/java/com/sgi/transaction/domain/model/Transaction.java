package com.sgi.transaction.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transaction")
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
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal balance;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedDate
    private Instant updatedDate;

}