package fr.ubordeaux.jmetrics.metrics.components;

public class Instability extends MetricsComponent {

    public Instability(double value) {
        super(value);
    }

    @Override
    protected boolean isValid(double value) {
        return value >= 0 && value <= 1;
    }

}
