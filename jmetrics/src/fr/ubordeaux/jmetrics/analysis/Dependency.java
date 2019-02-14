package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.metrics.GranularityScale;

import java.util.Objects;

/**
 * Value Object that represent a dependency between two class file.
 */
public class Dependency {

    private GranularityScale source;
    private GranularityScale destination;
    private DependencyType type;

    public Dependency(GranularityScale source, GranularityScale destination, DependencyType type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public GranularityScale getSource() {
        return source;
    }

    public GranularityScale getDestination() {
        return destination;
    }

    public DependencyType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return source.equals(that.source) &&
                destination.equals(that.destination) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination, type);
    }

}
