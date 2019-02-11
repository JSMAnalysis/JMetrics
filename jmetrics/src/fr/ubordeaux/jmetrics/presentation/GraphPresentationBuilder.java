package fr.ubordeaux.jmetrics.presentation;

import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.metrics.ClassCategory;

/**
 * Exposes an interface to build the representation of a graph as a String
 */
public interface GraphPresentationBuilder {

    void createNewGraph();

    void addNode(ClassCategory category);

    void addEdge(DependencyEdge edge);

    void endGraph();

    String getGraphPresentation();

}
