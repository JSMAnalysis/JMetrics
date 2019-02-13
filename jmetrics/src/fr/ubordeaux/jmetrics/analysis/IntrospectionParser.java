package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * A base class for parser using introspection. Provides a method to convert get a Class object from a ClassFile.
 */
public abstract class IntrospectionParser {

    private Map<ClassFile, SoftReference<Class<?>>> cache = new HashMap<>();

    public Class<?> getClassFromFile(ClassFile file){
        Class<?> c = cache.containsKey(file) ? cache.get(file).get() : null;
        if(c != null){
            return c;
        }
        byte[] byteCode;
        try {
            InputStream stream = file.getInputStream();
            byteCode = new byte[stream.available()];
            file.getInputStream().read(byteCode);
            stream.close();
        } catch (IOException e) {
            throw new ClassFileNotFoundException("The file associated with the class "
                    + file.getName()
                    + " does not seem to exist or is temporarily unavailable.");
        }
        ByteCodeClassLoader loader;
        try{
            URL projectRoot = (new File(ProjectStructure.getInstance().getrootPath()).toURI().toURL());
            loader = new ByteCodeClassLoader(projectRoot);
        }
        catch (MalformedURLException e){
            loader = new ByteCodeClassLoader();
        }
        c = loader.getClassFromByteCode(byteCode);
        cache.put(file, new SoftReference<>(c));
        return c;
    }

    /**
     * A class loader that is able to create a class object from an array of bytes.
     */
    private class ByteCodeClassLoader extends URLClassLoader {

        public ByteCodeClassLoader(){
            super(new URL[0]);
        }

        public ByteCodeClassLoader(URL url) {
            super(new URL[]{url});
        }

        public Class<?> getClassFromByteCode(byte[] bytes){
            return defineClass(null, bytes, 0, bytes.length);
        }
    }

}
