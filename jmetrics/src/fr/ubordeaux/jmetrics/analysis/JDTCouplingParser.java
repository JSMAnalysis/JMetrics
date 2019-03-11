package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.metrics.ClassGranule;
import fr.ubordeaux.jmetrics.metrics.Granule;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;
import org.eclipse.jdt.core.dom.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of the {@link CouplingParser} interface that uses JDT.
 */
public class JDTCouplingParser extends JDTParser implements CouplingParser {

    private Set<String> rawInheritanceDependencies = new HashSet<>();
    private Set<String> rawAggregationDependencies = new HashSet<>();
    private Set<String> rawUseLinkDependencies = new HashSet<>();

    @Override
    public List<Dependency> getDependencies(ClassFile srcFile) {
        rawInheritanceDependencies = new HashSet<>();
        rawAggregationDependencies = new HashSet<>();
        rawUseLinkDependencies = new HashSet<>();

        char[] sourceCode = getSourceCodeFromFile(srcFile);

        CompilationUnit comUnit = createAST(sourceCode, srcFile);

        comUnit.accept(this);

        List<ClassFile> projectClasses = ProjectStructure.getInstance().getClasses();
        List<Dependency> dependencies = new ArrayList<>();

        dependencies.addAll(generateDependenciesList(new ClassGranule(srcFile), DependencyType.Inheritance,
                rawInheritanceDependencies, projectClasses));
        dependencies.addAll(generateDependenciesList(new ClassGranule(srcFile), DependencyType.Aggregation,
                rawAggregationDependencies, projectClasses));
        dependencies.addAll(generateDependenciesList(new ClassGranule(srcFile), DependencyType.UseLink,
                rawUseLinkDependencies, projectClasses));

        return dependencies;
    }

    /**
     * Determine the list of efferent dependencies class that is present in project classes.
     * @param efferentDependencies List of class where the analyzed class depend upon.
     * @param projectClasses List of all class files in the project.
     * @return List of Granule (Elementary) such as efferent dependencies is in the project.
     */
    private List<Granule> findEfferentDependenciesInProject(Set<String> efferentDependencies,
                                                            List<ClassFile> projectClasses) {
        List<Granule> matchDependencies = new ArrayList<>();
        for (String classEff: efferentDependencies) {
            for (ClassFile dstFile: projectClasses) {
                if (dstFile.getName().equals(classEff)) {
                    Granule dst = new ClassGranule(dstFile);
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
    private List<Dependency> generateDependenciesList(Granule src, DependencyType type,
                                                      Set<String> dependencyList, List<ClassFile> projectClasses) {
        List<Dependency> dependencies = new ArrayList<>();
        dependencyList = filterClassName(dependencyList);
        List<Granule> dstList = findEfferentDependenciesInProject(dependencyList, projectClasses);
        //removes the class itself from its dependencies
        dstList.remove(src);
        for (Granule dst: dstList) {
            dependencies.add(new Dependency(src, dst, type));
        }
        return dependencies;
    }

    /**
     * Filters a set of class names by removing pieces of information that does not belong to the actual name.
     * This means the <...> genericity indicator and the [] array indicator.
     * @param classNames The Set to filter.
     * @return THe filtered Set
     */
    private Set<String> filterClassName(Set<String> classNames){
        return classNames.stream()
                .map( className -> className.replaceAll("(<.*>)|(\\[])", ""))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        ITypeBinding typeBinding = node.resolveBinding();

        //ignore internal and anonymous classes
        if(typeBinding.isNested()){
            return false;
        }

        //find dependencies from extends and implements declarations
        List<ITypeBinding> bindings = new ArrayList<>(Arrays.asList(typeBinding.getInterfaces()));
        if(typeBinding.getSuperclass() != null){
            bindings.add(typeBinding.getSuperclass());
        }

        for(ITypeBinding binding : bindings){
            rawInheritanceDependencies.add(binding.getQualifiedName());
        }

        return true;
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        ITypeBinding typeBinding = node.getType().resolveBinding();
        //find dependencies from fields declaration
        rawAggregationDependencies.add(typeBinding.getQualifiedName());

        return true;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        IMethodBinding methodBinding = node.resolveBinding();
        List<ITypeBinding> bindings = new ArrayList<>();
        //find dependencies from parameters, return type and exceptions thrown by a method
        bindings.addAll(Arrays.asList(methodBinding.getParameterTypes()));
        bindings.addAll(Arrays.asList(methodBinding.getExceptionTypes()));
        bindings.add(methodBinding.getReturnType());

        for(ITypeBinding binding : bindings){
            rawUseLinkDependencies.add(binding.getQualifiedName());
        }

        return true;
    }

    @Override
    public boolean visit(ClassInstanceCreation node) {
        //find dependencies from a class instance creation
        ITypeBinding typeBinding = node.resolveTypeBinding();

        rawUseLinkDependencies.add(typeBinding.getQualifiedName());

        return true;
    }

    @Override
    public boolean visit(VariableDeclarationStatement node) {
        //find dependencies from a variable declaration
        ITypeBinding typeBinding = node.getType().resolveBinding();

        rawUseLinkDependencies.add(typeBinding.getQualifiedName());

        return true;
    }

    @Override
    public boolean visit(ParameterizedType node) {

        //find dependencies contained in a generic type instance creation
        for(ITypeBinding binding : node.resolveBinding().getTypeArguments()){
            rawUseLinkDependencies.add(binding.getQualifiedName());
        }

        return true;
    }
}
