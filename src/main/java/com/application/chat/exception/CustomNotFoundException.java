package com.application.chat.exception;

public class CustomNotFoundException extends Exception{
    public CustomNotFoundException(String message) {
        super(message);
    }

    public CustomNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
