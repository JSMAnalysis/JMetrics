package fr.ubordeaux.jmetrics.metrics;

import java.util.Objects;

public abstract class GranularityScale {

    private String name;
    private Metrics metrics;

    public GranularityScale(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GranularityScale that = (GranularityScale) o;
        //TODO I think the metrics attributes is not part of the identity of the GranularityScale. Moreover, it causes some problems
        //because it does change the hash after adding metrics, which prevents the object from being retrieved in a HashMap.
        //This comment will be deleted when this question will have been properly discussed.
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
