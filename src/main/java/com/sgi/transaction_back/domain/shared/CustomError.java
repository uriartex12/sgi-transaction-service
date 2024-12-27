package com.sgi.transaction_back.domain.shared;

import com.sgi.transaction_back.infrastructure.exception.ApiError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomError {

    E_TRANSACTION_NOT_FOUND(new ApiError(HttpStatus.NOT_FOUND, "TRAN-001", "Transaction not found"));

    private final ApiError error;
}
