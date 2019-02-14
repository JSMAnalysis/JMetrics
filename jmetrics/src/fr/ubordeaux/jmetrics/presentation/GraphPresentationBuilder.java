package fr.ubordeaux.jmetrics.presentation;

import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.metrics.GranularityScale;

/**
 * Exposes an interface to build the representation of a graph as a String.
 * TODO: Documentation method
 */
public interface GraphPresentationBuilder {

    void createNewGraph();

    void addNode(GranularityScale node);

    void addEdge(DependencyEdge edge);

    void endGraph();

    String getGraphPresentation();

}
