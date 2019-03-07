package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.metrics.ClassGranularity;
import fr.ubordeaux.jmetrics.metrics.GranularityScale;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDTCouplingParser extends JDTParser implements CouplingParser {

    private List<String> inheritanceDependencies = new ArrayList<>();
    private List<String> aggregationDependancies = new ArrayList<>();
    private List<String> signatureDependencies = new ArrayList<>();
    private List<String> internalDependencies = new ArrayList<>();

    @Override
    public List<Dependency> getDependencies(ClassFile srcFile) {
        inheritanceDependencies = new ArrayList<>();
        aggregationDependancies = new ArrayList<>();
        signatureDependencies = new ArrayList<>();
        internalDependencies = new ArrayList<>();

        char[] sourceCode = getSourceCodeFromFile(srcFile);

        CompilationUnit comUnit = createAST(sourceCode, srcFile);

        comUnit.accept(this);

        List<ClassFile> projectClasses = ProjectStructure.getInstance().getClasses();
        List<Dependency> dependencies = new ArrayList<>();

        dependencies.addAll(generateDependenciesList(new ClassGranularity(srcFile), DependencyType.Inheritance,
                inheritanceDependencies, projectClasses));
        dependencies.addAll(generateDependenciesList(new ClassGranularity(srcFile), DependencyType.Aggregation,
                aggregationDependancies, projectClasses));
        dependencies.addAll(generateDependenciesList(new ClassGranularity(srcFile), DependencyType.Signature,
                signatureDependencies, projectClasses));
        dependencies.addAll(generateDependenciesList(new ClassGranularity(srcFile), DependencyType.Internal,
                internalDependencies, projectClasses));

        return dependencies;
    }

    /**
     * Determine the list of efferent dependencies class that is present in project classes.
     * @param efferentDependencies List of class where the analyzed class depend upon.
     * @param projectClasses List of all class files in the project.
     * @return List of GranularityScale (Elementary) such as efferent dependencies is in the project.
     */
    private List<GranularityScale> findEfferentDependenciesInProject(List<String> efferentDependencies,
                                                                     List<ClassFile> projectClasses) {
        List<GranularityScale> matchDependencies = new ArrayList<>();
        for (String classEff: efferentDependencies) {
            for (ClassFile dstFile: projectClasses) {
                if (dstFile.getName().equals(classEff)) {
                    GranularityScale dst = new ClassGranularity(dstFile);
                    matchDependencies.add(dst);
                }
            }
        }
        return matchDependencies;
    }

    /**
     * Generate a list of dependencies from a source category, a dependency type and a list of destination categories.
     * @param src The source category of the dependencies.
     * @param type The type of the dependencies.
     * @param dependencyList The list of destination class categories.
     * @return The list of generated dependencies.
     */
    private List<Dependency> generateDependenciesList(GranularityScale src, DependencyType type,
                                                      List<String> dependencyList, List<ClassFile> projectClasses) {
        List<Dependency> dependencies = new ArrayList<>();
        List<GranularityScale> dstList = new ArrayList<>();
        findEfferentDependenciesInProject(dependencyList, projectClasses);
        for (GranularityScale dst: dstList) {
            dependencies.add(new Dependency(src, dst, type));
        }
        return dependencies;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        ITypeBinding typeBinding = node.resolveBinding();

        List<ITypeBinding> bindings = new ArrayList<>(Arrays.asList(typeBinding.getInterfaces()));
        if(typeBinding.getSuperclass() != null){
            bindings.add(typeBinding.getSuperclass());
        }

        for(ITypeBinding binding : bindings){
            inheritanceDependencies.add(binding.getQualifiedName());
        }

        return true;
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        ITypeBinding typeBinding = node.getType().resolveBinding();

        aggregationDependancies.add(typeBinding.getQualifiedName());

        return true;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        IMethodBinding methodBinding = node.resolveBinding();
        List<ITypeBinding> bindings = new ArrayList<>();
        bindings.addAll(Arrays.asList(methodBinding.getParameterTypes()));
        bindings.addAll(Arrays.asList(methodBinding.getExceptionTypes()));
        bindings.add(methodBinding.getReturnType());

        for(ITypeBinding binding : bindings){
            signatureDependencies.add(binding.getQualifiedName());
        }

        return true;
    }
}
