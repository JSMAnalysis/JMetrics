package fr.ubordeaux.jmetrics.project;

public class InvalidProjectPathException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    InvalidProjectPathException(String message) {
        super(message);
    }

}
