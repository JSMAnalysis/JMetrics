package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.metrics.components.MetricsComponent;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class GranularityScale {

    private String name;
    private Set<MetricsComponent> metricsSet;

    public GranularityScale(String name){
        this.name = name;
        this.metricsSet = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Set<MetricsComponent> getMetricsSet(){
        return metricsSet;
    }

    public void addMetrics(MetricsComponent metrics){
        this.metricsSet.add(metrics);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GranularityScale that = (GranularityScale) o;
        return name.equals(that.name) &&
                metricsSet.equals(that.metricsSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, metricsSet);
    }
}
