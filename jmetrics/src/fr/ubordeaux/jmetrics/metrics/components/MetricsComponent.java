package fr.ubordeaux.jmetrics.metrics.components;

import fr.ubordeaux.jmetrics.metrics.BadMetricsValueException;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricsComponent that = (MetricsComponent) o;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
