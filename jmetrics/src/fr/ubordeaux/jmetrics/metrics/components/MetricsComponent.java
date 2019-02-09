package fr.ubordeaux.jmetrics.metrics.components;

import fr.ubordeaux.jmetrics.metrics.BadMetricsValueException;

public abstract class MetricsComponent {

    private double value;

    public MetricsComponent(double value) throws BadMetricsValueException {
        if(!isValid(value)) {
            throw new BadMetricsValueException("Given value does not satisfy constraint.");
        }
        this.value = value;
    }

    protected abstract boolean isValid(double value);

    public double getValue(){
        return value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MetricsComponent)){
            return false;
        }
        MetricsComponent other = (MetricsComponent) obj;
        return other.value == value;
    }

}
