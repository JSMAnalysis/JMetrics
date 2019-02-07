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

    public ClassFile(String name, File file){
        super(name);
        this.file = file;
    }

    public String getPath(){
        return file.getPath();
    }

    public InputStream getInputStream(){
        try {
            return new FileInputStream(file);
        }
        catch(IOException e){
            return null;
        }
    }

}
