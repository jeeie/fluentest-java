package com.fluentest;

public class TestFailureException extends Error {

    public TestFailureException() {
        super();
    }

    public TestFailureException(Throwable cause) {
        super(cause);
    }

    public TestFailureException(String message) {
        super(message);
    }

    public TestFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    protected TestFailureException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static void raiseException(String message) {
        throw new TestFailureException(message);
    }

    public static void throwException(Throwable cause) {
        if (cause instanceof TestFailureException) {
            throw (TestFailureException) cause;
        } else {
            throw new TestFailureException(cause);
        }
    }

    public static void throwException(String message, Throwable cause) {
        if (cause instanceof TestFailureException) {
            throw new TestFailureException(message + ": " + cause.getLocalizedMessage(), cause.getCause());
        } else {
            throw new TestFailureException(message, cause);
        }
    }

}
