package ground_truth;

import java.util.HashMap;
import java.util.Map;

public class ClassInformation {

    private int numberOfMethod;
    private int numberOfAbstractMethod;
    private Map<String, Double> metrics;

    ClassInformation(int numberOfMethod, int numberOfAbstractMethod,
                     double Ca, double Ce, double I, double A, double Dn) {
        this.numberOfMethod = numberOfMethod;
        this.numberOfAbstractMethod = numberOfAbstractMethod;
        this.metrics = new HashMap<>();
        metrics.put("Ca",   Ca);
        metrics.put("Ce",   Ce);
        metrics.put("I",    I);
        metrics.put("A",    A);
        metrics.put("Dn",   Dn);
    }

    public int getNumberOfMethod() {
        return numberOfMethod;
    }

    public int getNumberOfAbstractMethod() {
        return numberOfAbstractMethod;
    }

    public Map<String, Double> getMetrics() {
        return new HashMap<>(metrics);
    }

}
