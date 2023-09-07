package com.example.footballmanager.exception;

public class PlayerAlreadyOnTeamException extends BadRequestException {
    public PlayerAlreadyOnTeamException(String message) {
        super(message);
    }
}
