package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.project.ClassFile;
import org.eclipse.jdt.core.dom.*;

/**
 * An implementation of the {@link AbstractnessParser} interface that uses JDT.
 */
public class JDTAbstractnessParser extends JDTParser implements AbstractnessParser {

    private int abstractMethodsNumber;
    private int methodsNumber;

    @Override
    public AbstractnessData getAbstractnessData(ClassFile file) {
        abstractMethodsNumber = 0;
        methodsNumber = 0;
        char[] sourceCode = getSourceCodeFromFile(file);
        CompilationUnit comUnit = createAST(sourceCode, file, false);
        comUnit.accept(this);
        return new AbstractnessData(methodsNumber, abstractMethodsNumber);
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        // Visits all method declarations and count it
        for (MethodDeclaration methodDeclaration : node.getMethods()) {
            if (!methodDeclaration.isConstructor()) {
                if (Modifier.isAbstract(methodDeclaration.getModifiers()) || methodDeclaration.getBody() == null)
                    abstractMethodsNumber++;
                methodsNumber++;
            }
        }
        // Since we ignore internal and anonymous classes, don't visit the AST further than the top level class.
        return false;
    }

}
