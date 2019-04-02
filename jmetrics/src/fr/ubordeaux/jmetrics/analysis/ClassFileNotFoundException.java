package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;

public class ClassFileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MESSAGE_FORMAT =
            "The file associated with the class %s does not seem to exist or is temporarily unavailable. " +
                    "Did you provide the right project root ?";

    ClassFileNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new {@link ClassFileNotFoundException} referencing the faulty {@link ClassFile}
     * @param file The file that was not found.
     */
    ClassFileNotFoundException(ClassFile file){
        super(String.format(DEFAULT_MESSAGE_FORMAT, file.getFullyQualifiedName()));
    }

}
