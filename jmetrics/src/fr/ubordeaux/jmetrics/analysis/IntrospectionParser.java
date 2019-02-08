package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

import java.io.IOException;
import java.io.InputStream;

public abstract class IntrospectionParser {

    public Class getClassFromFile(ClassFile file){
        byte[] byteCode;
        try {
            InputStream stream = file.getInputStream();
            byteCode = new byte[stream.available()];
            file.getInputStream().read(byteCode);
            stream.close();
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
