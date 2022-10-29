package com.novruz.account.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.novruz.account.enums.CurrencyType;
import com.novruz.account.enums.TransactionDirection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends DefaultErrorAttributes {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handle(BadRequestException exception,
                                                      WebRequest request) {
        log.trace("Invalid request: {}", exception.getMessage());
        return ofType(request, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(OperationInvalidException.class)
    public ResponseEntity<Map<String, Object>> handle(OperationInvalidException exception,
                                                      WebRequest request) {
        log.trace("Invalid operation: {}", exception.getMessage());
        return ofType(request, HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handle(NotFoundException exception,
                                                      WebRequest request) {
        log.trace("Data not found: {}", exception.getMessage());
        return ofType(request, HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException exception,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ofType(request, HttpStatus.BAD_REQUEST, errors.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handle(HttpMessageNotReadableException exception,
                                                      WebRequest request) {
        log.info("Invalid request: {}", exception.getMessage());
        if (exception.getCause() instanceof InvalidFormatException) {
            var invalidFormatEx = (InvalidFormatException) exception.getCause();
            if (invalidFormatEx.getTargetType() != null && invalidFormatEx.getTargetType().isEnum()) {
                String message = exception.getMessage();
                var availableEnumValues = Arrays.toString(invalidFormatEx.getTargetType().getEnumConstants());
                if (invalidFormatEx.getTargetType() == CurrencyType.class) {
                    message = String.format("Invalid currency. The value must be one of '%s'", availableEnumValues);
                } else if (invalidFormatEx.getTargetType() == TransactionDirection.class) {
                    message = String.format("Invalid transaction direction. The value must be one of '%s'",
                            availableEnumValues);
                }
                return ofType(request, HttpStatus.BAD_REQUEST, message);
            }
        }
        return ofType(request, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    protected ResponseEntity<Map<String, Object>> ofType(WebRequest request, HttpStatus status, String message) {
        Map<String, Object> attributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        attributes.put("status", status.value());
        attributes.put("error", status.getReasonPhrase());
        attributes.put("message", message);
        attributes.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(attributes, status);
    }
}
