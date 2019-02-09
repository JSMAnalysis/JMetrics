package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

import java.util.List;

public interface CouplingParser {

    List<Dependency> getInheritanceDependency(ClassFile srcFile);
    List<Dependency> getAggregationDependencies(ClassFile srcFile);
    List<Dependency> getSignatureDependencies(ClassFile srcFile);
    List<Dependency> getInternalDependencies(ClassFile srcFile);

}
