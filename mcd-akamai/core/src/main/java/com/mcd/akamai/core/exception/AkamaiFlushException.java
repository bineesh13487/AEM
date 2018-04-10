package com.mcd.akamai.core.exception;

public class AkamaiFlushException extends Exception {
    public AkamaiFlushException() {
    }

    public AkamaiFlushException(String message) {
        super(message);
    }

    public AkamaiFlushException(String message, Throwable cause) {
        super(message, cause);
    }

    public AkamaiFlushException(Throwable cause) {
        super(cause);
    }
}
