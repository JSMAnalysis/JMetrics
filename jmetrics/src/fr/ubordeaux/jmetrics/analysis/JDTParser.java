package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.IOException;
import java.nio.file.Files;

public abstract class JDTParser extends ASTVisitor{

    public char[] getSourceCodeFromFile(ClassFile file){
        byte[] code;
        try {
            code = Files.readAllBytes(file.getFile().toPath());
        } catch (IOException e) {
            throw new ClassFileNotFoundException("The file associated with the class "
                    + file.getName()
                    + " does not seem to exist or is temporarily unavailable.");
        }

        return (new String(code).toCharArray());
    }

    public CompilationUnit createAST(char[] sourceCode, ClassFile srcFile){
        ASTParser parser = ASTParser.newParser(8);
        parser.setSource(sourceCode);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setEnvironment(new String[]{}, new String[]{ProjectStructure.getInstance().getRootPath()}, null, false);
        parser.setUnitName(srcFile.getFile().getPath());
        return (CompilationUnit) parser.createAST(null);
    }

}
