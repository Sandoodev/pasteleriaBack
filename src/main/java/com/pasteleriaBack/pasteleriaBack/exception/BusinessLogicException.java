package com.pasteleriaBack.pasteleriaBack.exception;

public class BusinessLogicException extends RuntimeException {

    public BusinessLogicException(String message) {
        super(message); // Llama al constructor de RuntimeException con el mensaje
    }
}