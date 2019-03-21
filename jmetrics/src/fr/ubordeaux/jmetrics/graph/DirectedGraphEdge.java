package fr.ubordeaux.jmetrics.graph;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectedGraphEdge<?> that = (DirectedGraphEdge<?>) o;
        return source.equals(that.source) &&
                target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }

}
