package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.analysis.AbstractnessData;
import fr.ubordeaux.jmetrics.graph.DependencyEdge;
import fr.ubordeaux.jmetrics.graph.DirectedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MartinMetrics extends Metrics {

    private final static String CA = "Ca";
    private final static String CE = "Ce";
    private final static String A = "A";
    private final static String I = "I";
    private final static String DN = "Dn";

    private int getAfferentCoupling() { return getIntMetrics(CA); }
    private int getEfferentCoupling() { return getIntMetrics(CE); }
    private double getAbstractness() { return getDoubleMetrics(A); }
    private double getInstability() { return getDoubleMetrics(I); }
    private double getNormalizedDistance() { return getDoubleMetrics(DN); }

    private Function<Integer, Boolean> isValidCoupling = value -> (value >= 0 && value == Math.floor(value));
    private Function<Double, Boolean> isValidComponents = value -> (value >= 0 && value <= 1);

    /**
     * Compute metrics of a given granule (class scale).
     * @param g The class granule on which calculate the metrics.
     * @param aData The granule's abstractness data.
     * @param graph The class scale graph.
     */
    public void computeClassMetrics(ClassGranule g, AbstractnessData aData, DirectedGraph<Granule, DependencyEdge> graph) {
        setAfferentCoupling(graph, g);
        setEfferentCoupling(graph, g);
        setInstability(getAfferentCoupling(), getEfferentCoupling());
        setAbstractness(aData);
        setNormalizedDistance(getAbstractness(), getInstability());
    }

    /**
     * Compute metrics of a given granule (package scale).
     * The instability calculus does not consider dependency's multiplicity.
     * The package's abstractness is calculated through formula : AVERAGE(Abstractness_Class) for class in the package.
     * @param g The package granule on which calculate the metrics.
     * @param graph The package scale graph.
     */
    public void computePackageMetrics(PackageGranule g, DirectedGraph<Granule, DependencyEdge> graph) {
        setAfferentCoupling(graph, g);
        setEfferentCoupling(graph, g);
        setInstability(getAfferentCoupling(), getEfferentCoupling());
        List<Granule> gContent = g.getContent();
        double abstractnessSum = gContent.stream()
                .filter(o -> o instanceof ClassGranule)
                .mapToDouble(o -> ((MartinMetrics)o.getMetrics()).getAbstractness())
                .sum();
        setAbstractness(abstractnessSum / gContent.size());
        setNormalizedDistance(getAbstractness(), getInstability());
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

    private void setAfferentCoupling(DirectedGraph<Granule, DependencyEdge> graph, Granule granule) {
        int afferentCoupling = graph.getIncomingEdgesList(granule).size();
        if (!isValidCoupling.apply(afferentCoupling)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
        addMetric(CA, afferentCoupling);
    }

    private void setEfferentCoupling(DirectedGraph<Granule, DependencyEdge> graph, Granule granule) {
        int efferentCoupling = graph.getOutcomingEdgeList(granule).size();
        if (!isValidCoupling.apply(efferentCoupling)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
        addMetric(CE, efferentCoupling);
    }

    private void setInstability(int Ca, int Ce) {
        double instability = (Ca + Ce) == 0 ? 0 : (double)Ce / (double)(Ca + Ce);
        instability = roundValue(instability);
        if (!isValidComponents.apply(instability)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
        addMetric(I, instability);
    }

    private void setNormalizedDistance(double abstractness, double instability) {
        double normalizedDistance = Math.abs(abstractness + instability - 1);
        normalizedDistance = roundValue(normalizedDistance);
        if (!isValidComponents.apply(normalizedDistance)) {
            throw new BadMetricsValueException(BadMetricsValueException.DEFAULT_MESSAGE);
        }
        addMetric(DN, normalizedDistance);
    }

    @Override
    public List<String> getCSVCaption() {
        List<String> caption = new ArrayList<>();
        caption.add(CA);
        caption.add(CE);
        caption.add(I);
        caption.add(A);
        caption.add(DN);
        return caption;
    }

    @Override
    public List<String> getCSVExposedData() {
        List<String> exposedData = new ArrayList<>();
        exposedData.add(Integer.toString(getAfferentCoupling()));
        exposedData.add(Integer.toString(getEfferentCoupling()));
        exposedData.add(Double.toString(getInstability()));
        exposedData.add(Double.toString(getAbstractness()));
        exposedData.add(Double.toString(getNormalizedDistance()));
        return exposedData;
    }

}
