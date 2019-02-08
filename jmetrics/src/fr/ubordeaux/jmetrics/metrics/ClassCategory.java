package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.metrics.components.MetricsComponent;
import fr.ubordeaux.jmetrics.project.ProjectComponent;

import java.util.Set;

public abstract class ClassCategory {

    protected String name;
    protected ProjectComponent category;
    protected Set<MetricsComponent> metricsSet;

    public String getName() {
        return name;
    }

}
