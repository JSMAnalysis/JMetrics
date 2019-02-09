package fr.ubordeaux.jmetrics.metrics.components;

public class EfferentCoupling extends MetricsComponent {

    public EfferentCoupling(double value) {
        super(value);
    }

    @Override
    protected boolean isValid(double value) {
        return value >= 0 && value == Math.floor(value);
    }

}
