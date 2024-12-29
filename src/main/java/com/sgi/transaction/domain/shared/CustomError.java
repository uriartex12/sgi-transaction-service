package com.sgi.transaction.domain.shared;

import com.sgi.transaction.infrastructure.exception.ApiError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enum representing custom errors for the Transaction-service application.
 * Each constant includes an error code, message, and HTTP status for specific errors.
 */
@Getter
@AllArgsConstructor
public enum CustomError {

    E_TRANSACTION_NOT_FOUND(new ApiError(HttpStatus.NOT_FOUND, "TRAN-001", "Transaction not found"));

    private final ApiError error;
}
