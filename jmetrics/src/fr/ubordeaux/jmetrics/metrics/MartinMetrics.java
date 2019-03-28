package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.analysis.AbstractnessData;
import fr.ubordeaux.jmetrics.graph.DependencyEdge;
import fr.ubordeaux.jmetrics.graph.DirectedGraph;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MartinMetrics extends Metrics{

    private final static int PRECISION = 2;
    private final static String CA = "Ca";
    private final static String CE = "Ce";
    private final static String A = "A";
    private final static String I = "I";
    private final static String DN = "Dn";


    private Function<Integer, Boolean> isValidCoupling = value -> (value >= 0 && value == Math.floor(value));
    private Function<Double, Boolean> isValidComponents = value -> (value >= 0 && value <= 1);

    private double roundValue(double value) {
        double shift = Math.pow(10, PRECISION);
        return Math.round(value * shift) / shift;
    }

    public void computeClassMetrics(Granule g, AbstractnessData aData, DirectedGraph<Granule, DependencyEdge> graph) {
        setAfferentCoupling(graph, g);
        setEfferentCoupling(graph, g);
        setInstability(getAfferentCoupling(), getEfferentCoupling());
        setAbstractness(aData);
        setNormalizedDistance(getAbstractness(), getInstability());
    }

    public void computePackageMetrics(Granule g, DirectedGraph<Granule, DependencyEdge> graph) {
        setAfferentCoupling(graph, g);
        setEfferentCoupling(graph, g);
        // TODO: Instability calculus should consider dependencies arity.
        setInstability(getAfferentCoupling(), getEfferentCoupling());
        List<Granule> gContent = ((PackageGranule)g).getContent();
        // TODO: Should we consider the abstractness of a package as :
        //  1. SUM(nbAbstractMethod_Class) / SUM(nbMethod_Class)
        //  2. AVERAGE(Abstractness_Class) [Solution actually implemented]
        double abstractnessSum = gContent.stream()
                .filter(o -> o instanceof ClassGranule)
                .mapToDouble(o -> o.getMetrics().getAbstractness())
                .sum();
        setAbstractness(abstractnessSum / gContent.size());
        setNormalizedDistance(getAbstractness(), getInstability());
    }



    double getAbstractness() {
        return getDoubleMetrics(A);
    }

    private void setAbstractness(AbstractnessData data) {
        double abstractness = data.getNumberOfMethods() == 0 ? 0 : (double)data.getNumberOfAbstractMethods() / (double)data.getNumberOfMethods();
        abstractness = roundValue(abstractness);
        if (!isValidComponents.apply(abstractness)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
        addMetric(A, abstractness);
    }

    private void setAbstractness(double value) {
        value = roundValue(value);
        if (!isValidComponents.apply(value)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
        addMetric(A, value);
    }

    int getAfferentCoupling() {
        return getIntMetrics(CA);
    }

    private void setAfferentCoupling(DirectedGraph<Granule, DependencyEdge> graph, Granule granule) {
        int afferentCoupling = graph.getIncomingEdgesList(granule).size();
        if (!isValidCoupling.apply(afferentCoupling)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
        addMetric(CA, afferentCoupling);
    }

    int getEfferentCoupling() {
        return getIntMetrics(CE);
    }

    private void setEfferentCoupling(DirectedGraph<Granule, DependencyEdge> graph, Granule granule) {
        int efferentCoupling = graph.getOutcomingEdgeList(granule).size();
        if (!isValidCoupling.apply(efferentCoupling)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
        addMetric(CE, efferentCoupling);
    }

    double getInstability() {
        return getDoubleMetrics(I);
    }

    private void setInstability(int Ca, int Ce) {
        double instability = (Ca + Ce) == 0 ? 0 : (double)Ce / (double)(Ca + Ce);
        instability = roundValue(instability);
        if (!isValidComponents.apply(instability)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
        addMetric(I, instability);
    }

    double getNormalizedDistance() {
        return getDoubleMetrics(DN);
    }

    private void setNormalizedDistance(double abstractness, double instability) {
        double normalizedDistance = Math.abs(abstractness + instability - 1);
        normalizedDistance = roundValue(normalizedDistance);
        if (!isValidComponents.apply(normalizedDistance)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
        addMetric(DN, normalizedDistance);
    }

}
