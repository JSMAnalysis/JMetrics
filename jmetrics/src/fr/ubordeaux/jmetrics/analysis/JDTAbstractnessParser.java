package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.datastructure.AbstractnessData;
import fr.ubordeaux.jmetrics.project.ClassFile;
import org.eclipse.jdt.core.dom.*;


public class JDTAbstractnessParser extends JDTParser implements AbstractnessParser {

    private int abstractMethodsNumber;
    private int methodsNumber;

    @Override
    public AbstractnessData getAbstractnessData(ClassFile file) {

        abstractMethodsNumber = 0;
        methodsNumber = 0;

        char[] sourceCode = getSourceCodeFromFile(file);
        CompilationUnit comUnit = createAST(sourceCode, file);

        comUnit.accept(this);

        return new AbstractnessData(methodsNumber, abstractMethodsNumber);
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        if(Modifier.isAbstract(node.getModifiers()) || node.getBody() == null)
            abstractMethodsNumber++;
        methodsNumber++;
        return false;
    }


}
