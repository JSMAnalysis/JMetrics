package ground_truth;

import fr.ubordeaux.jmetrics.analysis.DependencyType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Dependencies.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Dependency {

    Class<?> dependencyTo();
    DependencyType type();
    boolean sourceOnly() default false;

}
