package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.metrics.components.MetricsComponent;
import fr.ubordeaux.jmetrics.project.ProjectComponent;

import java.util.HashSet;
import java.util.Set;

public abstract class ClassCategory {

    private String name;
    private ProjectComponent component;
    private Set<MetricsComponent> metricsSet;

    public ClassCategory(String name, ProjectComponent component){
        this.name = name;
        this.component = component;
        this.metricsSet = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public ProjectComponent getComponent(){
        return component;
    }

    public Set<MetricsComponent> getMetricsSet(){
        return metricsSet;
    }

    public void addMetrics(MetricsComponent metrics){
        this.metricsSet.add(metrics);
    }

}
