package com.example.footballmanager.exception;

public class TeamAlreadyExistsException extends BadRequestException {
    public TeamAlreadyExistsException(String message) {
        super(message);
    }
}
