package com.mate.academy.demo.exception;

public class OrderProcessingException extends RuntimeException {
    public OrderProcessingException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public OrderProcessingException(String message) {
        super(message);
    }
}
