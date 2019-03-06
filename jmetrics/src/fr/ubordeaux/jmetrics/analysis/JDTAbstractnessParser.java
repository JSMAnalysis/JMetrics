package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.datastructure.AbstractnessData;
import fr.ubordeaux.jmetrics.project.ClassFile;
import org.eclipse.jdt.core.dom.*;


import java.io.IOException;
import java.io.InputStream;

public class JDTAbstractnessParser extends ASTVisitor implements AbstractnessParser {

    private int abstractMethodsNumber;
    private int methodsNumber;

    @Override
    public AbstractnessData getAbstractnessData(ClassFile file) {

        abstractMethodsNumber = 0;
        methodsNumber = 0;

        byte[] sourceCode;
        try {
            InputStream stream = file.getInputStream();
            sourceCode = new byte[stream.available()];
            file.getInputStream().read(sourceCode);
            stream.close();
        } catch (IOException e) {
            throw new ClassFileNotFoundException("The file associated with the class "
                    + file.getName()
                    + " does not seem to exist or is temporarily unavailable.");
        }

        ASTParser parser = ASTParser.newParser(8);
        parser.setSource((new String(sourceCode)).toCharArray());
        CompilationUnit comUnit = (CompilationUnit) parser.createAST(null);

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
