package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;

/**
 * A base class for parser using introspection. Provides a method to get a Class object from a ClassFile.
 */
public abstract class IntrospectionParser {

    private ByteCodeSandboxClassLoader loader = null;

    /**
     * Returns a Class object representing the class contained in the given ClassFile.
     * @param file The file that contains the class to load.
     * @return The loaded class.
     */
    Class<?> getClassFromFile(ClassFile file) {

        if (loader == null) {
            try {
                URL projectRoot = (new File(ProjectStructure.getInstance().getRootPath()).toURI().toURL());
                loader = new ByteCodeSandboxClassLoader(projectRoot);
            } catch(MalformedURLException e){
                loader = new ByteCodeSandboxClassLoader();
            }
        }

        Class<?> c = loader.getClassFromName(file.getFullyQualifiedName());
        if(c != null){
            return c;
        }


        byte[] byteCode;
        try {
            byteCode = Files.readAllBytes(file.getFile().toPath());
        } catch (IOException e) {
            throw new ClassFileNotFoundException(file);
        }

        c = loader.getClassFromByteCode(byteCode, file.getFullyQualifiedName());
        if(c == null) throw new ClassFileNotFoundException(file);
        return c;
    }

    /**
     * A class loader that is able to create a class object from an array of bytes.
     * It is also able to load a class that is already loader, even if their versions differ.
     */
    private static class ByteCodeSandboxClassLoader extends URLClassLoader {

        ByteCodeSandboxClassLoader(){
            super(new URL[0]);
        }

        ByteCodeSandboxClassLoader(URL url) {
            super(new URL[]{url});
        }

        Class<?> getClassFromName(String className){
            try{
                return loadClass(className);
            }
            catch (ClassNotFoundException | NoClassDefFoundError e) {
                return null;
            }
        }

        Class<?> getClassFromByteCode(byte[] bytes, String className){
            try{
                return loadClass(className);
            }
            catch (ClassNotFoundException | NoClassDefFoundError e) {
                try {
                    return defineClass(className, bytes, 0, bytes.length);
                }
                catch(NoClassDefFoundError error){
                    return null;
                }
            }
        }

    }

}
