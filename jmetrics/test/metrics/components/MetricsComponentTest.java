package metrics.components;

import fr.ubordeaux.jmetrics.datastructure.AbstractnessData;
import fr.ubordeaux.jmetrics.metrics.BadMetricsValueException;
import fr.ubordeaux.jmetrics.metrics.components.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MetricsComponentTest {

    @Test
    void testCreateMetricsWithRightValue() {
        MetricsComponent component;
        try {
            component = new Abstractness(new AbstractnessData(0,0));
            component = new Abstractness(new AbstractnessData(10,0));
            component = new Abstractness(new AbstractnessData(10,10));
            component = new Abstractness(new AbstractnessData(100,63));

            component = new Instability(0, 0);
            component = new Instability(0, 10);
            component = new Instability(10, 0);
            component = new Instability(100, 63);

            component = new NormalizedDistance(0, 1);
            component = new NormalizedDistance(0, 0);
            component = new NormalizedDistance(1, 0.63);
        } catch (BadMetricsValueException e){
            fail("No exception should be throw when metrics is built with correct values");
        }
    }

    @Test
    void testCreateMetricsWithWrongValue() {
        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new Abstractness(new AbstractnessData(10, 12));
        });
        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new Abstractness(new AbstractnessData(-10, 5));
        });

        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new Instability(10, -5);
        });
        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new Instability(-5, 10);
        });

        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new NormalizedDistance(2,2);
        });
        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new NormalizedDistance(-1, -1);
        });
    }

}