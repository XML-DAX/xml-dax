package com.dax.exception;

/**
 *
 */
public class UncheckedXMLStreamException extends RuntimeException {
    public UncheckedXMLStreamException() {
    }

    public UncheckedXMLStreamException(String message) {
        super(message);
    }

    public UncheckedXMLStreamException(String message, Throwable cause) {
        super(message, cause);
    }

    public UncheckedXMLStreamException(Throwable cause) {
        super(cause);
    }

    public UncheckedXMLStreamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
