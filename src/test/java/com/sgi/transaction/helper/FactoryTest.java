package com.sgi.transaction.helper;

import com.sgi.transaction.domain.model.Transaction;
import com.sgi.transaction.infrastructure.dto.AverageReportResponse;
import com.sgi.transaction.infrastructure.dto.Product;
import com.sgi.transaction.infrastructure.dto.TransactionRequest;
import com.sgi.transaction.infrastructure.dto.TransactionResponse;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

/**
 * Class containing methods to generate TransactionRequest and TransactionResponse objects
 * with default values to facilitate unit testing.
 */
public class FactoryTest {

    /**
     * Generates an TransactionRequest object with default values for testing.
     *
     * @return An TransactionRequest object with configured values for testing.
     */
    @SneakyThrows
    public static <R> R toFactoryTransaction(Class<R> response) {
        R account = response.getDeclaredConstructor().newInstance();
        if (account instanceof TransactionRequest) {
            return (R) initializeAccount((TransactionRequest) account);
        } else if (account instanceof TransactionResponse) {
            return (R) initializeAccount((TransactionResponse) account);
        }
        return account;
    }

    private static TransactionRequest initializeAccount(TransactionRequest transaction) {
        transaction.setAmount(10D);
        transaction.setClientId(UUID.randomUUID().toString());
        transaction.setType(TransactionRequest.TypeEnum.DEPOSIT);
        transaction.setCommission(2D);
        transaction.setProductId(UUID.randomUUID().toString());
        transaction.setDestinationProductId(UUID.randomUUID().toString());
        return transaction;
    }

    private static TransactionResponse initializeAccount(TransactionResponse transaction) {
        transaction.setAmount(BigDecimal.TEN);
        transaction.setClientId(UUID.randomUUID().toString());
        transaction.setType(TransactionResponse.TypeEnum.DEPOSIT);
        transaction.setCommission(2D);
        transaction.setProductId(UUID.randomUUID().toString());
        transaction.setDestinationProductId(UUID.randomUUID().toString());
        return transaction;
    }

    /**
     * Creates a list of TransactionResponse objects with default values for testing purposes.
     *
     * @param productId The product ID to associate with the transaction response.
     * @return A list containing a single TransactionResponse object with preset values.
     */
    public static List<TransactionResponse> toFactoryListTransactionResponse(String productId) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setClientId(randomUUID().toString());
        transactionResponse.setAmount(BigDecimal.valueOf(100));
        transactionResponse.setType(TransactionResponse.TypeEnum.DEPOSIT);
        transactionResponse.setProductId(productId);
        transactionResponse.setDestinationProductId(null);
        transactionResponse.setCreatedDate(OffsetDateTime.now());
        transactionResponse.setBalance(BigDecimal.valueOf(10).doubleValue());
        return List.of(transactionResponse);
    }

    /**
     * Generates a sample {@link AverageReportResponse} object with a random client ID and a list of products.
     * Each product contains a random product ID and an empty list of daily averages.
     * This method is typically used for testing purposes to create mock data.
     *
     * @return A mock {@link AverageReportResponse} with a list of products.
     */
    public static AverageReportResponse toFactoryAverageReportResponse() {
        Product product = new Product();
        product.setProductId(randomUUID().toString());
        product.setDailyAverages(List.of());

        List<Product> products = List.of(product);
        AverageReportResponse averageReportResponse = new AverageReportResponse();
        averageReportResponse.clientId(randomUUID().toString());
        averageReportResponse.setProducts(products);
        return  averageReportResponse;
    }

    /**
     * Creates a new Transaction object with predefined values for testing purposes.
     *
     * @return A Transaction object populated with random and default values, such as a client ID, balance,
     *         transaction type, and other attributes.
     */
    public static Transaction toFactoryEntityTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(10));
        transaction.setBalance(BigDecimal.ONE);
        transaction.setType("DEPOSIT");
        transaction.setCreatedDate(Instant.now());
        transaction.setProductId(UUID.randomUUID().toString());
        transaction.setClientId(UUID.randomUUID().toString());
        return transaction;
    }


}
