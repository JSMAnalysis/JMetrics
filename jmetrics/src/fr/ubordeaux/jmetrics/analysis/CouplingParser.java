package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

import java.util.List;

/**
 * Provides an interface to access some dependencies of a class.
 */
public interface CouplingParser {

    /**
     * Retrieve all types of efferent dependencies of a given class.
     * @param srcFile The class to analyze
     * @return The list of all dependencies.
     */
    List<Dependency> getDependencies(ClassFile srcFile);

}
