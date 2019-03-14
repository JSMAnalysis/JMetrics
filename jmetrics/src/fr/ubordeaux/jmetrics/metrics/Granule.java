package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.presentation.CSVRepresentable;
import fr.ubordeaux.jmetrics.project.ProjectComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Granule implements CSVRepresentable {

    protected ProjectComponent relatedComponent;
    protected String fullyQualifiedName;
    protected String displayName;
    protected Metrics metrics;

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public String getDisplayName(){
        return displayName;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public ProjectComponent getRelatedComponent() {
        return relatedComponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Granule that = (Granule)o;
        return Objects.equals(fullyQualifiedName, that.fullyQualifiedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullyQualifiedName);
    }

    @Override
    public List<String> getCSVCaption() {
        return new ArrayList<String>() {{
            add("Granule"); add("Ca"); add("Ce"); add("I"); add("A"); add("Dn");
        }};
    }

    @Override
    public List<String> getCSVExposedData() {
        Metrics m = getMetrics();
        return new ArrayList<String>() {{
            add(getDisplayName());
            add(Integer.toString(m.getAfferentCoupling()));
            add(Integer.toString(m.getEfferentCoupling()));
            add(Double.toString(m.getInstability()));
            add(Double.toString(m.getAbstractness()));
            add(Double.toString(m.getNormalizedDistance()));
        }};
    }

}
