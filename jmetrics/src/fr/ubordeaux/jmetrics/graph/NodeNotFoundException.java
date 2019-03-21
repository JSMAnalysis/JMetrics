package fr.ubordeaux.jmetrics.graph;

/**
 * Exception thrown when a client try to access a node that does not exist in a graph.
 */
public class NodeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    NodeNotFoundException(String message) {
        super(message);
    }

}
