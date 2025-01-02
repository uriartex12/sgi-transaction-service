package com.sgi.transaction.infrastructure.exception;

import com.sgi.transaction.infrastructure.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * GlobalExceptionHandler handles custom exceptions in the application.
 * It uses @ControllerAdvice to intercept thrown exceptions and generate appropriate error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles the custom exception and returns a response with the status code and error message.
     *
     * @param ex Custom exception.
     * @return Mono encapsulating the error response.
     */
    @ExceptionHandler(CustomException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleCustomException(CustomException ex) {
        return Mono.just(ResponseEntity
                .status(ex.getStatus())
                .body(createErrorResponse(ex)));
    }

    /**
     * Creates an ErrorResponse object from the custom exception.
     *
     * @param ex Custom exception.
     * @return An ErrorResponse object with the error details.
     */
    private ErrorResponse createErrorResponse(CustomException ex) {
        LocalDateTime localDateTime = ex.getTimestamp();
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);
        return new ErrorResponse(ex.getStatus(),
                ex.getCode(), ex.getMessage(), offsetDateTime);
    }
}
