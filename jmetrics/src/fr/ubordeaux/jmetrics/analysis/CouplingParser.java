package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

import java.util.List;

/**
 * TODO: Let only one method accessible.
 */
public interface CouplingParser {

    /**
     * Retrieve efferent dependencies of type Inheritance of a given class.
     * @param srcFile The file to analyze.
     * @return The list of Inheritance dependencies.
     */
    List<Dependency> getInheritanceDependencies(ClassFile srcFile);

    /**
     * Retrieve efferent dependencies of type Aggregation of a given class.
     * @param srcFile The file to analyze.
     * @return The list of Aggregation dependencies.
     */
    List<Dependency> getAggregationDependencies(ClassFile srcFile);

    /**
     * Retrieve efferent dependencies of type Signature of a given class.
     * @param srcFile The file to analyze.
     * @return The list of Signature dependencies.
     */
    List<Dependency> getSignatureDependencies(ClassFile srcFile);

    /**
     * Retrieve efferent dependencies of type Internal of a given class.
     * @param srcFile The file to analyze.
     * @return The list of Internal dependencies.
     */
    List<Dependency> getInternalDependencies(ClassFile srcFile);

}
