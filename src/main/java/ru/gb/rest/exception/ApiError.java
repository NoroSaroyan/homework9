package ru.gb.rest.exception;

public class ApiError {

    private final int statusCode;
    private final String message;

    public ApiError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}