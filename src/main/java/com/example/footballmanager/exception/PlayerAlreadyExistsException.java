package com.example.footballmanager.exception;

public class PlayerAlreadyExistsException extends BadRequestException {
    public PlayerAlreadyExistsException(String message) {
        super(message);
    }
}
