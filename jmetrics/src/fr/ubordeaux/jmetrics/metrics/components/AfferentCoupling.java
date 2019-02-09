package fr.ubordeaux.jmetrics.metrics.components;

public class AfferentCoupling extends MetricsComponent {

    public AfferentCoupling(double value) {
        super(value);
    }

    @Override
    protected boolean isValid(double value) {
        return value >= 0 && value == Math.floor(value);
    }

}
