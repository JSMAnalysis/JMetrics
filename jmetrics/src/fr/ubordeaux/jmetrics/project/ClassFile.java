package fr.ubordeaux.jmetrics.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a .class File (containing a Java class' ByteCode).
 */
public class ClassFile extends ProjectComponent {

    public ClassFile(File file){
        super(file);
    }

    /**
     * Opens a stream on the file property.
     * Note that the caller is responsible for closing the stream.
     * @return Stream that point to the file.
     */
    public InputStream getInputStream(){
        try {
            return new FileInputStream(getFile());
        }
        catch(IOException e){
            return null;
        }
    }

}
