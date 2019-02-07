package fr.ubordeaux.jmetrics.metrics.components;

public class EfferentCoupling extends AbstractMetricsComponent {

    public EfferentCoupling(double value) {
        super(value);
    }

    @Override
    protected boolean isValid(double value) {
        if(value >= 0 && value == Math.floor(value) ){
            return true;
        }
        return false;
    }
}
