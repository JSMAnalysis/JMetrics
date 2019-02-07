package fr.ubordeaux.jmetrics.datastructure;

import java.util.Objects;

/**
 * Defines the base data structure used to store information about a directed graph's edge.
 */
public class DirectedGraphEdge {
    private DirectedGraphNode source;
    private DirectedGraphNode target;

    public DirectedGraphEdge(DirectedGraphNode source, DirectedGraphNode target){
        this.source = source;
        this.target = target;
    }

    public DirectedGraphNode getSource() {
        return source;
    }

    public DirectedGraphNode getTarget() {
        return target;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DirectedGraphEdge)){
            return false;
        }
        DirectedGraphEdge other = (DirectedGraphEdge) obj;
        return this.source.equals(other.source)
                && this.target.equals(other.target);
    }
}
