package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.project.ClassFile;

public class ElementaryCategory extends ClassCategory {

    public ElementaryCategory(ClassFile file) {
        super(file.getName(), file);
    }

}
