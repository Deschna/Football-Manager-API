package com.example.footballmanager.exception;

public class InsufficientBudgetException extends RuntimeException {
    public InsufficientBudgetException(String message) {
        super(message);
    }
}
