package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.project.ProjectComponent;

import java.util.Objects;

public abstract class Granule {

    protected ProjectComponent relatedComponent;
    protected String name;
    protected Metrics metrics;

    public String getName() {
        return name;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public ProjectComponent getRelatedComponent() {
        return relatedComponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Granule that = (Granule)o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
