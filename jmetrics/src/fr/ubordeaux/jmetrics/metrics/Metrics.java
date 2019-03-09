package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.datastructure.AbstractnessData;
import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.datastructure.DirectedGraph;

import java.util.Objects;
import java.util.function.Function;

public class Metrics {

    private final static int PRECISION = 2;

    private Function<Integer, Boolean> isValidCoupling = value -> (value >= 0 && value == Math.floor(value));
    private Function<Double, Boolean> isValidComponents = value -> (value >= 0 && value <= 1);

    private double abstractness;
    private int afferentCoupling;
    private int efferentCoupling;
    private double instability;
    private double normalizedDistance;

    private double roundValue(double value) {
        double shift = Math.pow(10, PRECISION);
        return Math.round(value * shift) / shift;
    }

    public double getAbstractness() {
        return abstractness;
    }

    public void setAbstractness(AbstractnessData data) {
        abstractness = data.getNumberOfMethods() == 0 ? 0 : (double)data.getNumberOfAbstractMethods() / (double)data.getNumberOfMethods();
        abstractness = roundValue(abstractness);
        if (!isValidComponents.apply(abstractness)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
    }

    public int getAfferentCoupling() {
        return afferentCoupling;
    }

    public void setAfferentCoupling(DirectedGraph<Granule, DependencyEdge> graph, Granule granule) {
        afferentCoupling = graph.getIncomingEdgesList(granule).size();
        if (!isValidCoupling.apply(afferentCoupling)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
    }

    public int getEfferentCoupling() {
        return efferentCoupling;
    }

    public void setEfferentCoupling(DirectedGraph<Granule, DependencyEdge> graph, Granule granule) {
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
        instability = roundValue(instability);
        if (!isValidComponents.apply(instability)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
    }

    public double getNormalizedDistance() {
        return normalizedDistance;
    }

    public void setNormalizedDistance(double abstractness, double instability) {
        normalizedDistance = Math.abs(abstractness + instability - 1);
        normalizedDistance = roundValue(normalizedDistance);
        if (!isValidComponents.apply(normalizedDistance)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metrics metrics = (Metrics) o;
        return Double.compare(metrics.abstractness, abstractness) == 0 &&
                afferentCoupling == metrics.afferentCoupling &&
                efferentCoupling == metrics.efferentCoupling &&
                Double.compare(metrics.instability, instability) == 0 &&
                Double.compare(metrics.normalizedDistance, normalizedDistance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(abstractness, afferentCoupling, efferentCoupling, instability, normalizedDistance);
    }
}
