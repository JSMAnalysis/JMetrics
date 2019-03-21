package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

/**
 * Provides an interface to access some basic data describing the abstractness of a class.
 */
public interface AbstractnessParser {

    /**
     * Retrieve abstractness data (number of methods, number of abstract methods) of a given class.
     * @param file The class to analyze.
     * @return The abstractness data of the class.
     */
    AbstractnessData getAbstractnessData(ClassFile file);

}
