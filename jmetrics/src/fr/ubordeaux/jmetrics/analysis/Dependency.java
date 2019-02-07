package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

/**
 * Value Object that represent a dependency between two class file.
 */
public class Dependency {

    private ClassFile source;
    private ClassFile destination;
    private DependencyType type;

    public Dependency(ClassFile source, ClassFile destination, DependencyType type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public ClassFile getSource() {
        return source;
    }

    public ClassFile getDestination() {
        return destination;
    }

    public DependencyType getType() {
        return type;
    }

}
