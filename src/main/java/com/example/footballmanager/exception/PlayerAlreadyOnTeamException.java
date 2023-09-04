package com.example.footballmanager.exception;

public class PlayerAlreadyOnTeamException extends RuntimeException {
    public PlayerAlreadyOnTeamException(String message) {
        super(message);
    }
}
