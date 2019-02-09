package fr.ubordeaux.jmetrics.metrics.components;

public class NormalizedDistance extends MetricsComponent {

    public NormalizedDistance(double value) {
        super(value);
    }

    @Override
    protected boolean isValid(double value) {
        return value >= 0 && value <= 1;
    }

}
