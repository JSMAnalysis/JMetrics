package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An implementation of the {@link AbstractnessParser} interface that uses introspection.
 */
public class IntrospectionAbstractnessParser extends IntrospectionParser implements AbstractnessParser {

    @Override
    public AbstractnessData getAbstractnessData(ClassFile file) {
        Class<?> c = getClassFromFile(file);
        return new AbstractnessData(getNumberOfMethods(c),getNumberOfAbstractMethods(c));
    }

    private int getNumberOfMethods(Class<?> c) {
        if(c.isEnum()){
            return removeEnumMethods(c.getDeclaredMethods()).size();
        }
        return c.getDeclaredMethods().length;
    }

    private int getNumberOfAbstractMethods(Class<?> c) {
        int abstractCount = 0;
        List<Method> methods;
        if(c.isEnum()){
            methods = removeEnumMethods(c.getDeclaredMethods());
        }
        else{
            methods = Arrays.asList(c.getDeclaredMethods());
        }
        for (Method method : methods) {
            if(Modifier.isAbstract(method.getModifiers())) {
                abstractCount++;
            }
        }
        return abstractCount;
    }

    private List<Method> removeEnumMethods(Method[] methods){
        List<Method> methodsList = new ArrayList<>();
        for(Method m : methods){
            if(!m.getName().equals("values") && !m.getName().equals("valueOf")){
                methodsList.add(m);
            }
        }
        return methodsList;
    }

}
