package fr.ubordeaux.jmetrics.metrics;

public class BadMetricsValueException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_MESSAGE = "The given data does not satisfy the metric constraint.";

    public BadMetricsValueException(String message) {
        super(message);
    }

}
