package fr.ubordeaux.jmetrics.metrics.components;

import fr.ubordeaux.jmetrics.datastructure.AbstractnessData;

public class Abstractness extends MetricsComponent {

    public Abstractness(AbstractnessData data) {
        super(data.getNumberOfMethods() == 0 ? 0 : data.getNumberOfAbstractMethods() / data.getNumberOfMethods());
    }

    @Override
    protected boolean isValid(double value) {
        return value >= 0 && value <= 1;
    }
}