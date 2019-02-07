package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

/**
 * An implementation of the {@link AbstractnessParser} interface that uses introspection.
 */
public class IntrospectionAbstractnessParser implements AbstractnessParser{

    @Override
    public int getNumberOfMethods(ClassFile file) {
        getClassFromFile(file);
        return 0;
    }

    @Override
    public int getNumberOfAbstractMethods(ClassFile file) {
        return 0;
    }

    private Class getClassFromFile(ClassFile file){
        //TODO Nécessité d'implémenter le ClassFile afin de pouvoir en extraire le flux d'entrée.
        //on suppose avoir un tableau de bytes et on nom
        String className = "Une classe";
        byte[] byteCode = new byte[42];
        ByteCodeClassLoader loader = new ByteCodeClassLoader();
        return loader.getClassFromByteCode(className, byteCode);
    }



    private class ByteCodeClassLoader extends ClassLoader{
        public Class getClassFromByteCode(String name, byte[] bytes){
            return defineClass(name, bytes, 0, bytes.length);
        }
    }
}
