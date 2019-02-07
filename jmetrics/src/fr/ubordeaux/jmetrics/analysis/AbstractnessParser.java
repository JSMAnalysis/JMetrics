package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

/**
 * Provides an interface to access some basic data describing the abstractness of a class.
 */
public interface AbstractnessParser {

    int getNumberOfMethods(ClassFile file);
    int getNumberOfAbstractMethods(ClassFile file);

}
