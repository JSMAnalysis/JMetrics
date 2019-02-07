package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * An implementation of the {@link AbstractnessParser} interface that uses introspection.
 */
public class IntrospectionAbstractnessParser implements AbstractnessParser{

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

    private Class getClassFromFile(ClassFile file){
        byte[] byteCode;
        try {
            byteCode = new byte[file.getInputStream().available()];
            file.getInputStream().read(byteCode);
        }
        catch (IOException e){
            throw new ClassFileNotFoundException("The file associated with the class "
                    + file.getName()
                    + " does not seem to exist or is temporarily unavailable.");
        }
        ByteCodeClassLoader loader = new ByteCodeClassLoader();
        return loader.getClassFromByteCode(file.getName(), byteCode);
    }


    /**
     * A class loader that is able to create a class object from an array of bytes.
     */
    private class ByteCodeClassLoader extends ClassLoader{
        public Class getClassFromByteCode(String name, byte[] bytes){
            return defineClass(name, bytes, 0, bytes.length);
        }
    }
}
