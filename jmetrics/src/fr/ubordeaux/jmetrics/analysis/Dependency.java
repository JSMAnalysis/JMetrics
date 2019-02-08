package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.metrics.ClassCategory;
import fr.ubordeaux.jmetrics.project.ClassFile;

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

}
