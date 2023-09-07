package com.example.footballmanager.exception;

public class InsufficientBudgetException extends BadRequestException {
    public InsufficientBudgetException(String message) {
        super(message);
    }
}
