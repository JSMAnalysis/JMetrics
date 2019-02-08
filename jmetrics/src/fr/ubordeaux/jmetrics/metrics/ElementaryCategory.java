package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.project.ClassFile;

public class ElementaryCategory extends ClassCategory {

    public ElementaryCategory(ClassFile file) {
        this.category = file;
        this.name = file.getName();
    }

}
