package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.presentation.CSVRepresentable;
import fr.ubordeaux.jmetrics.project.ProjectComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Granule implements CSVRepresentable {

    private ProjectComponent relatedComponent;
    private String fullyQualifiedName;
    private String displayName;
    private Metrics metrics = null;

    protected Granule(ProjectComponent relatedComponent, String fullyQualifiedName, String displayName) {
        this.relatedComponent = relatedComponent;
        this.fullyQualifiedName = fullyQualifiedName;
        this.displayName = displayName;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public ProjectComponent getRelatedComponent() {
        return relatedComponent;
    }

    public int computeDepth(){
        return fullyQualifiedName.split(".").length;
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
        List<String> caption = new ArrayList<>();
        caption.add("Granule");
        caption.add("Ca");
        caption.add("Ce");
        caption.add("I");
        caption.add("A");
        caption.add("Dn");
        return caption;
    }

    @Override
    public List<String> getCSVExposedData() {
        Metrics m = getMetrics();
        List<String> exposedData = new ArrayList<>();
        exposedData.add(getDisplayName());
        exposedData.add(Integer.toString(m.getAfferentCoupling()));
        exposedData.add(Integer.toString(m.getEfferentCoupling()));
        exposedData.add(Double.toString(m.getInstability()));
        exposedData.add(Double.toString(m.getAbstractness()));
        exposedData.add(Double.toString(m.getNormalizedDistance()));
        return exposedData;
    }

}
