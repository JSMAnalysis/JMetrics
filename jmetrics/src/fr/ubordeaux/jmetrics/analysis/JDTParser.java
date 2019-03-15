package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * A base class for parsers using JDT. Provides methods to get a class source code from a source file and create an AST.
 */
public abstract class JDTParser extends ASTVisitor{

    /**
     * Reads the source code of a class from a .java file.
     * @param file The file that contains the source code.
     * @return An array of chars containing the source code.
     */
    char[] getSourceCodeFromFile(ClassFile file) {
        byte[] code;
        try {
            code = Files.readAllBytes(file.getFile().toPath());
        } catch (IOException e) {
            throw new ClassFileNotFoundException("The file associated with the class "
                    + file.getFullyQualifiedName()
                    + " does not seem to exist or is temporarily unavailable.");
        }
        return (new String(code).toCharArray());
    }

    /**
     * Creates a JDT AST from a source code and the corresponding source file.
     * @param sourceCode The source code to use to build the AST.
     * @param srcFile The source file to use to build the AST.
     * @return The created AST.
     */
    CompilationUnit createAST(char[] sourceCode, ClassFile srcFile) {
        ASTParser parser = ASTParser.newParser(AST.JLS11);
        Map<String,String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
        parser.setCompilerOptions(options);
        parser.setSource(sourceCode);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setEnvironment(new String[]{}, new String[]{ProjectStructure.getInstance().getRootPath()}, null, true);
        parser.setUnitName(srcFile.getFile().getPath());
        return (CompilationUnit) parser.createAST(null);
    }

}
