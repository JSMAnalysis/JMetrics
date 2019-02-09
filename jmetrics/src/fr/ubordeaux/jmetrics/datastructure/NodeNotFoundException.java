package fr.ubordeaux.jmetrics.datastructure;

/**
 * Exception thrown when a client try to access a node that does not exist in a graph.
 */
public class NodeNotFoundException extends RuntimeException {

    public NodeNotFoundException(String message){
        super(message);
    }

}
