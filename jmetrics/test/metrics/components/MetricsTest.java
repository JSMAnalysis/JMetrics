package metrics.components;

import fr.ubordeaux.jmetrics.datastructure.AbstractnessData;
import fr.ubordeaux.jmetrics.metrics.BadMetricsValueException;
import fr.ubordeaux.jmetrics.metrics.Metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricsTest {

    private Metrics metrics;

    @BeforeEach
    void setUp() {
        metrics = new Metrics();
    }

    @Test
    void testSetMetricsRightValue() {
        try {
            metrics.setAbstractness(new AbstractnessData(0,0));
            metrics.setAbstractness(new AbstractnessData(10,0));
            metrics.setAbstractness(new AbstractnessData(10,10));
            metrics.setAbstractness(new AbstractnessData(100,63));

            metrics.setInstability(0, 0);
            metrics.setInstability(0, 10);
            metrics.setInstability(10, 0);
            metrics.setInstability(100, 63);

            metrics.setNormalizedDistance(0, 1);
            metrics.setNormalizedDistance(0, 0);
            metrics.setNormalizedDistance(1, 0.63);
        } catch (BadMetricsValueException e){
            fail("No exception should be throw when metrics is built with correct values");
        }
    }

    @Test
    void testSetMetricsWrongValue() {
        assertThrows(BadMetricsValueException.class, ()-> metrics.setAbstractness(new AbstractnessData(10, 12)));
        assertThrows(BadMetricsValueException.class, ()-> metrics.setAbstractness(new AbstractnessData(-10, 5)));

        assertThrows(BadMetricsValueException.class, ()-> metrics.setInstability(10, -5));
        assertThrows(BadMetricsValueException.class, ()-> metrics.setInstability(-5, 10));

        assertThrows(BadMetricsValueException.class, ()-> metrics.setNormalizedDistance(2,2));
        assertThrows(BadMetricsValueException.class, ()-> metrics.setNormalizedDistance(-1, -1));
    }

}