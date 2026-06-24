package com.qa.framework.exceptions;

/**
 * Custom exception for when elements are not found
 */
public class ElementNotFoundException extends FrameworkException {

    public ElementNotFoundException(String message) {
        super(message);
    }

    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
