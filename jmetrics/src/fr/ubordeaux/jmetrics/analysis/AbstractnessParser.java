package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

public interface AbstractnessParser {

    int getNumberOfMethods(ClassFile file);
    int getNumberOfAbstractMethods(ClassFile file);

}
