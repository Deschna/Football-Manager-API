package com.example.footballmanager.exception;

import java.util.NoSuchElementException;

public class PlayerNotFoundException extends NoSuchElementException {
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
