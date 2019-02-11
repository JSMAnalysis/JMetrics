package fr.ubordeaux.jmetrics.metrics.components;

public class Instability extends MetricsComponent {

    public Instability(double Ce, double Ca) {
        super((Ca + Ce) == 0 ? 0 : Ce / (Ca + Ce));
    }

    @Override
    protected boolean isValid(double value) {
        return value >= 0 && value <= 1;
    }

}
