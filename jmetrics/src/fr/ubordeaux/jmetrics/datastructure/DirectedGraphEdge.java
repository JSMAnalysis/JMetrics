package fr.ubordeaux.jmetrics.datastructure;

import java.util.Objects;

/**
 * Defines the base data structure used to store information about a directed graph's edge.
 */
public class DirectedGraphEdge<N> {

    private N source;
    private N target;

    public DirectedGraphEdge(N source, N target){
        this.source = source;
        this.target = target;
    }

    public N getSource() {
        return source;
    }

    public N getTarget() {
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
        return this.source.equals(other.source) && this.target.equals(other.target);
    }

}
