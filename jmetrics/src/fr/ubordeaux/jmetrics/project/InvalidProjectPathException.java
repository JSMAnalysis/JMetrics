package fr.ubordeaux.jmetrics.project;

public class InvalidProjectPathException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    InvalidProjectPathException() {
        super();
    }

    InvalidProjectPathException(String message) {
        super(message);
    }

    InvalidProjectPathException(Throwable cause) {
        super(cause);
    }

    InvalidProjectPathException(String message, Throwable cause) {
        super(message, cause);
    }
}
