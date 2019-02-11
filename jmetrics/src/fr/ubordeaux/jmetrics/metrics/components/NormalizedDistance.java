package fr.ubordeaux.jmetrics.metrics.components;

public class NormalizedDistance extends MetricsComponent {

    public NormalizedDistance(double abstractness, double instability) {
        super(Math.abs(abstractness + instability - 1));
    }

    @Override
    protected boolean isValid(double value) {
        return value >= 0 && value <= 1;
    }

}
