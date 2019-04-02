package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

/**
 * A base class for parsers using JDT. Provides methods to get a class source code from a source file and create an AST.
 */
public abstract class JDTParser extends ASTVisitor{

    private Charset charset = StandardCharsets.UTF_8; //use UTF_8 by default. Maybe we should expose an option to
                                                      //tweak it.
    private PackageNameChecker packageNameChecker = new PackageNameChecker();

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
            throw new ClassFileNotFoundException(file);
        }
        return (new String(code, charset).toCharArray());
    }

    /**
     * Creates a JDT AST from a source code and the corresponding source file.
     * @param sourceCode The source code to use to build the AST.
     * @param srcFile The source file to use to build the AST.
     * @return The created AST.
     */
    CompilationUnit createAST(char[] sourceCode, ClassFile srcFile, boolean needBindings) {
        ASTParser parser = ASTParser.newParser(AST.JLS11);
        Map<String,String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
        parser.setCompilerOptions(options);
        parser.setSource(sourceCode);
        parser.setResolveBindings(needBindings);
        parser.setBindingsRecovery(needBindings);
        parser.setEnvironment(new String[]{}, new String[]{ProjectStructure.getInstance().getRootPath()}, null, true);
        parser.setUnitName(srcFile.getFile().getPath());
        CompilationUnit ast = (CompilationUnit) parser.createAST(null);
        if(!packageNameChecker.packagesCorrespond(srcFile, ast)){
            throw new ClassFileNotFoundException(srcFile);
        }
        return ast;
    }


    /**
     * A service providing a method to checks that a ClassFile's package is the same as its associated source file.
     */
    private class PackageNameChecker extends ASTVisitor{

        private boolean corresponds;
        private String expectedPackage = "";

        boolean packagesCorrespond(ClassFile srcFile, CompilationUnit classAST) {
            corresponds = true;
            expectedPackage = srcFile.getFullyQualifiedName()
                    .substring(0, srcFile.getFullyQualifiedName().lastIndexOf('.'));
            classAST.accept(this);
            return corresponds;
        }

        @Override
        public boolean visit(PackageDeclaration node) {
            if (!node.getName().getFullyQualifiedName().equals(expectedPackage)) {
                corresponds = false;
            }
            return false;
        }

    }

}
