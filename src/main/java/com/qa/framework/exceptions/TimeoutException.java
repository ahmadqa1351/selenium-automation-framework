package com.qa.framework.exceptions;

/**
 * Custom exception for timeout scenarios
 */
public class TimeoutException extends FrameworkException {

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
