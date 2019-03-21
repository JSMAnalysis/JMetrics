package fr.ubordeaux.jmetrics.graph;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import fr.ubordeaux.jmetrics.metrics.Granule;

import java.util.Objects;

public class DependencyEdge extends DirectedGraphEdge<Granule> {

    private DependencyType type;

    DependencyEdge(Granule source, Granule target, DependencyType type) {
        super(source, target);
        this.type = type;
    }

    public DependencyType getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DependencyEdge that = (DependencyEdge) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }

}
