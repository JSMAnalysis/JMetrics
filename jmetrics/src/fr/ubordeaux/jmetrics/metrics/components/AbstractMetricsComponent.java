package fr.ubordeaux.jmetrics.metrics.components;

import fr.ubordeaux.jmetrics.metrics.BadMetricsValueException;

public abstract class AbstractMetricsComponent implements MetricsComponent{

    private double value;

    public AbstractMetricsComponent(double value) throws BadMetricsValueException{
        if(!isValid(value)) {
            throw new BadMetricsValueException();
        }
        this.value = value;
    }

    protected abstract boolean isValid(double value);

    @Override
    public double getValue(){
        return value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AbstractMetricsComponent)){
            return false;
        }
        AbstractMetricsComponent other = (AbstractMetricsComponent) obj;
        return other.value == value;
    }
}
