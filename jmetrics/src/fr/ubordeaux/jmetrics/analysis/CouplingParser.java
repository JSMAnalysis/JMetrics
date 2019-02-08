package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

import java.util.List;

public interface CouplingParser {

    List<Dependency> getInheritanceDependency(ClassFile file);
    List<Dependency> getAggregationDependencies(ClassFile file);
    List<Dependency> getSignatureDependencies(ClassFile file);
    List<Dependency> getInternalDependencies(ClassFile file);

}
