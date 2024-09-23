package com.antoniocarlos.amelia;
/**
 * Custom Exception that signals a violation of the lit/map nming conventions for  the language.
 * Extends IllegalArgumentException.
 */
public class IllegalKeyTypeException extends IllegalArgumentException {

    /**
     * Default no-args constructor.
     */
    public IllegalKeyTypeException() {
        super();
    }

    /**
     * Error message accepting constructor.
     *
     * @param message A message describing the exception.
     */
    public IllegalKeyTypeException(String message) {
        super(message);
    }

    /**
     * Error message and cause accepting constructor.
     *
     * @param message An error message that describes the exception.
     * @param cause The Exception's cause (a throwable).
     */
    public IllegalKeyTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *  Cause Accepting Constructor.
     *
     * @param cause The exception's cause (a throwable).
     */
    public IllegalKeyTypeException(Throwable cause) {
        super(cause);
    }
}