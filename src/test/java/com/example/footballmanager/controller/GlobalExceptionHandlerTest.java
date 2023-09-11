package com.example.footballmanager.controller;

import com.example.footballmanager.exception.BadRequestException;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler exceptionHandler;
    private static final String DEFAULT_ERROR_MESSAGE = "Error Message";

    public GlobalExceptionHandlerTest() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleBadRequestException() {
        BadRequestException ex = new BadRequestException(DEFAULT_ERROR_MESSAGE);

        ResponseEntity<Object> responseEntity = exceptionHandler.handleBadRequestException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(DEFAULT_ERROR_MESSAGE, responseBody.get("error"));
    }

    @Test
    public void testHandleNoSuchElementException() {
        NoSuchElementException ex = new NoSuchElementException(DEFAULT_ERROR_MESSAGE);

        ResponseEntity<Object> responseEntity = exceptionHandler.handleNoSuchElementException(ex);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(DEFAULT_ERROR_MESSAGE, responseBody.get("error"));
    }
}
