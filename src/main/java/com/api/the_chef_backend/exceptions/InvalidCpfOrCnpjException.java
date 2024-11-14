package com.api.the_chef_backend.exceptions;

public class InvalidCpfOrCnpjException extends RuntimeException {
    public InvalidCpfOrCnpjException(String message) {
        super(message);
    }
}
