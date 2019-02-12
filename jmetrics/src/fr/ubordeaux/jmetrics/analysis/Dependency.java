package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.metrics.ClassCategory;

import java.util.Objects;

/**
 * Value Object that represent a dependency between two class file.
 */
public class Dependency {

    private ClassCategory source;
    private ClassCategory destination;
    private DependencyType type;

    public Dependency(ClassCategory source, ClassCategory destination, DependencyType type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public ClassCategory getSource() {
        return source;
    }

    public ClassCategory getDestination() {
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
