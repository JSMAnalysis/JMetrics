package fr.ubordeaux.jmetrics.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represent .class File (containing ByteCode).
 */
public class ClassFile extends ProjectComponent {
    private File file;

    public ClassFile(File file){
        super(file);
    }

    public String getPath(){
        return file.getPath();
    }

    /**
     * Open a stream on the file property.
     * Note that the caller is responsible for closing the stream.
     * @return Stream that point to the file.
     */
    public InputStream getInputStream(){
        try {
            return new FileInputStream(file);
        }
        catch(IOException e){
            return null;
        }
    }

}
