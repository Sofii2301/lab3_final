package com.lab3_final.lab3_final.persistence.exception;

public class CircularDependencyException extends Exception {
    public CircularDependencyException(String message) {
        super(message);
    }
}
