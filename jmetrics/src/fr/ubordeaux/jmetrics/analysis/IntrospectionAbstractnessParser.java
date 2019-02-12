package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.datastructure.AbstractnessData;
import fr.ubordeaux.jmetrics.project.ClassFile;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * An implementation of the {@link AbstractnessParser} interface that uses introspection.
 */
public class IntrospectionAbstractnessParser extends IntrospectionParser implements AbstractnessParser {

    @Override
    public AbstractnessData getAbstractnessData(ClassFile file) {
        Class c = getClassFromFile(file);
        return new AbstractnessData(getNumberOfMethods(c),getNumberOfAbstractMethods(c));
    }

    private int getNumberOfMethods(Class c) {
        return c.getDeclaredMethods().length;
    }

    private int getNumberOfAbstractMethods(Class c) {
        int abstractCount = 0;
        for(Method method : c.getDeclaredMethods()){
            if(Modifier.isAbstract(method.getModifiers())){
                abstractCount++;
            }
        }
        return abstractCount;
    }

}
