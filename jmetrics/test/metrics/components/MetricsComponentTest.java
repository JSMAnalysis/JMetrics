package metrics.components;

import fr.ubordeaux.jmetrics.metrics.BadMetricsValueException;
import fr.ubordeaux.jmetrics.metrics.components.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MetricsComponentTest {

    @Test
    void testCreateMetricsWithRightValue() {
        MetricsComponent component;
        try {
            component = new Abstractness(0);
            component = new Abstractness(1);
            component = new Abstractness(0.536);

            component = new Instability(0);
            component = new Instability(1);
            component = new Instability(0.536);

            component = new AfferentCoupling(0);
            component = new AfferentCoupling(5);

            component = new EfferentCoupling(0);
            component = new EfferentCoupling(5);

            component = new NormalizedDistance(0);
            component = new NormalizedDistance(1);
            component = new NormalizedDistance(0.536);
        } catch (BadMetricsValueException e){
            fail("No exception should be throw when metrics is built with correct values");
        }
    }

    @Test
    void testCreateMetricsWithWrongValue() {
        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new Abstractness(1.2);
        });
        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new Abstractness(-1);
        });

        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new Instability(1.2);
        });
        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new Instability(-1);
        });

        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new EfferentCoupling(-1);
        });
        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new EfferentCoupling(0.2);
        });

        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new AfferentCoupling(-1);
        });
        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new AfferentCoupling(0.2);
        });

        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new NormalizedDistance(1.2);
        });
        assertThrows(BadMetricsValueException.class, ()->{
            MetricsComponent component = new NormalizedDistance(-1);
        });
    }

}