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
        return name.equals(that.name) &&
                metrics.equals(that.metrics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, metrics);
    }

}
