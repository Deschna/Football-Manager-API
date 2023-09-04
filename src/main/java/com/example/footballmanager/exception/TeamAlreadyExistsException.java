package com.example.footballmanager.exception;

public class TeamAlreadyExistsException extends IllegalArgumentException {
    public TeamAlreadyExistsException(String message) {
        super(message);
    }
}
