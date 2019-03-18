package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * A base class for parser using introspection. Provides a method to get a Class object from a ClassFile.
 */
public abstract class IntrospectionParser {

    private Map<ClassFile, SoftReference<Class<?>>> cache = new HashMap<>();

    Class<?> getClassFromFile(ClassFile file){
        Class<?> c = cache.containsKey(file) ? cache.get(file).get() : null;
        if (c != null) {
            return c;
        }

        byte[] byteCode;
        try {
            byteCode = Files.readAllBytes(file.getFile().toPath());
        } catch (IOException e) {
            throw new ClassFileNotFoundException(file);
        }
        ByteCodeClassLoader loader;
        try {
            URL projectRoot = (new File(ProjectStructure.getInstance().getRootPath()).toURI().toURL());
            loader = new ByteCodeClassLoader(projectRoot);
        } catch (MalformedURLException e) {
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

        ByteCodeClassLoader(){
            super(new URL[0]);
        }

        ByteCodeClassLoader(URL url) {
            super(new URL[]{url});
        }

        Class<?> getClassFromByteCode(byte[] bytes){
            return defineClass(null, bytes, 0, bytes.length);
        }

    }

}
