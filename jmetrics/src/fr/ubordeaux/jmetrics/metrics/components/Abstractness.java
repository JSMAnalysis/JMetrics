package fr.ubordeaux.jmetrics.metrics.components;

public class Abstractness extends MetricsComponent {

    public Abstractness(double value) {
        super(value);
    }

    @Override
    protected boolean isValid(double value) {
        return value >= 0 && value <= 1;
    }

}