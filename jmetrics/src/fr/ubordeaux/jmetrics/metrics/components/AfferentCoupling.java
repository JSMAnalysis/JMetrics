package fr.ubordeaux.jmetrics.metrics.components;

public class AfferentCoupling extends AbstractMetricsComponent {

    public AfferentCoupling(double value) {
        super(value);
    }

    @Override
    protected boolean isValid(double value) {
        if(value >= 0 && value == Math.floor(value)){
            return true;
        }
        return false;
    }
}
