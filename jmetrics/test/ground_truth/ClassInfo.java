package ground_truth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClassInfo {
    int numberOfMethod();
    int numberOfAbstractMethod();
    int Ca();
    int Ce();
    double I();
    double A();
    double Dn();
}
