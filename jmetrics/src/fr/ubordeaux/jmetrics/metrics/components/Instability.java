package fr.ubordeaux.jmetrics.metrics.components;

public class Instability extends AbstractMetricsComponent {

    public Instability(double value) {
        super(value);
    }

    @Override
    public double getValue() {
        return 0;
    }

    @Override
    protected boolean isValid(double value) {
        if(value >= 0 && value <= 1){
            return true;
        }
        return false;
    }
}
