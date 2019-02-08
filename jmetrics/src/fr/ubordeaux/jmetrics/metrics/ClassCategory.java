package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.metrics.components.MetricsComponent;

import java.util.Set;

public abstract class ClassCategory {

    private String name;
    private Set<MetricsComponent> metricsSet;

    public String getName() {
        return name;
    }

}
