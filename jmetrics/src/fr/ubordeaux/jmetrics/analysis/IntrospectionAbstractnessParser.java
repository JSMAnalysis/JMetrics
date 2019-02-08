package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * An implementation of the {@link AbstractnessParser} interface that uses introspection.
 */
public class IntrospectionAbstractnessParser extends IntrospectionParser implements AbstractnessParser {

    @Override
    public int getNumberOfMethods(ClassFile file) {
        return getClassFromFile(file).getDeclaredMethods().length;
    }

    @Override
    public int getNumberOfAbstractMethods(ClassFile file) {
        Class c = getClassFromFile(file);
        int abstractCount = 0;
        for(Method method : c.getDeclaredMethods()){
            if(Modifier.isAbstract(method.getModifiers())){
                abstractCount++;
            }
        }
        return abstractCount;
    }

}
