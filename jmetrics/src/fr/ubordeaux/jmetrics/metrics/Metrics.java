package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.datastructure.AbstractnessData;
import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.datastructure.DirectedGraph;

import java.util.function.Function;

public class Metrics {

    private Function<Integer, Boolean> isValidCoupling = value -> (value >= 0 && value == Math.floor(value));
    private Function<Double, Boolean> isValidComponents = value -> (value >= 0 && value <= 1);

    private double abstractness;
    private int afferentCoupling;
    private int efferentCoupling;
    private double instability;
    private double normalizedDistance;

    public double getAbstractness() {
        return abstractness;
    }

    public void setAbstractness(AbstractnessData data) {
        abstractness = data.getNumberOfMethods() == 0 ? 0 : (double)data.getNumberOfAbstractMethods() / (double)data.getNumberOfMethods();
        if (!isValidComponents.apply(abstractness)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
    }

    public int getAfferentCoupling() {
        return afferentCoupling;
    }

    public void setAfferentCoupling(DirectedGraph<GranularityScale, DependencyEdge> graph, GranularityScale granule) {
        afferentCoupling = graph.getIncomingEdgesList(granule).size();
        if (!isValidCoupling.apply(afferentCoupling)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
    }

    public int getEfferentCoupling() {
        return efferentCoupling;
    }

    public void setEfferentCoupling(DirectedGraph<GranularityScale, DependencyEdge> graph, GranularityScale granule) {
        efferentCoupling = graph.getOutcomingEdgeList(granule).size();
        if (!isValidCoupling.apply(efferentCoupling)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
    }

    public double getInstability() {
        return instability;
    }

    public void setInstability(double Ce, double Ca) {
        instability = (Ca + Ce) == 0 ? 0 : Ce / (Ca + Ce);
        if (!isValidComponents.apply(instability)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
    }

    public double getNormalizedDistance() {
        return normalizedDistance;
    }

    public void setNormalizedDistance(double abstractness, double instability) {
        normalizedDistance = Math.abs(abstractness + instability - 1);
        if (!isValidComponents.apply(normalizedDistance)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
    }

}
