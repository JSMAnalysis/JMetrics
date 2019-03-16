package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.metrics.Granule;
import fr.ubordeaux.jmetrics.presentation.CSVRepresentable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Value Object that represent a dependency between two class file.
 */
public class Dependency implements CSVRepresentable {

    private Granule source;
    private Granule destination;
    private DependencyType type;
    private int arity;

    public Dependency(Granule source, Granule destination, DependencyType type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.arity = 1;
    }

    public int getArity() {
        return arity;
    }

    public void incrementArity() {
        arity++;
    }

    public Granule getSource() {
        return source;
    }

    public Granule getDestination() {
        return destination;
    }

    public DependencyType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return source.equals(that.source) &&
                destination.equals(that.destination) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination, type);
    }

    @Override
    public List<String> getCSVCaption() {
        List<String> caption = new ArrayList<>();
        caption.add("GranuleSrc");
        caption.add("GranuleDst");
        caption.add("Type");
        caption.add("Arity");
        return caption;
    }

    @Override
    public List<String> getCSVExposedData() {
        List<String> exposedData = new ArrayList<>();
        exposedData.add(source.getDisplayName());
        exposedData.add(destination.getDisplayName());
        exposedData.add(type.toString());
        exposedData.add(Integer.toString(arity));
        return exposedData;
    }

}
