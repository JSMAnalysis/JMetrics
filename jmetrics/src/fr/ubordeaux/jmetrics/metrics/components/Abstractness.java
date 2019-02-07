package fr.ubordeaux.jmetrics.metrics.components;

// corresponds to the metric A in Martin's
public class Abstractness extends AbstractMetricsComponent {


    public Abstractness(double value) {
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