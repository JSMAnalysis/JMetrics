package fr.ubordeaux.jmetrics.metrics.components;

public class NormalizedDistance extends AbstractMetricsComponent {

    public NormalizedDistance(double value) {
        super(value);
    }

    @Override
    protected boolean isValid(double value) {
        if(value >= 0 && value <= 1){
            return true;
        }
        return false;
    }
}
