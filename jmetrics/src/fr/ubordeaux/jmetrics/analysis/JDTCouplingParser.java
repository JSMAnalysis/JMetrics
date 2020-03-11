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

    private Set<String> rawInheritanceDependencies;
    private Set<String> rawAssociationDependencies;
    private Set<String> rawUseLinkDependencies;
    private String currentAnalyzedClassName;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Dependency> getDependencies(ClassFile srcFile) {
        rawInheritanceDependencies = new HashSet<>();
        rawAssociationDependencies = new HashSet<>();
        rawUseLinkDependencies = new HashSet<>();
        currentAnalyzedClassName = srcFile.getFullyQualifiedName();

        CompilationUnit comUnit = createAST(srcFile, true);
        comUnit.accept(this);

        List<ClassFile> projectClasses = ProjectStructure.getInstance().getClasses();
        List<Dependency> dependencies = new ArrayList<>();

        dependencies.addAll(generateDependenciesList(new ClassGranule(srcFile), DependencyType.Inheritance,
                rawInheritanceDependencies, projectClasses));
        dependencies.addAll(generateDependenciesList(new ClassGranule(srcFile), DependencyType.Association,
                rawAssociationDependencies, projectClasses));
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
                if (dstFile.getFullyQualifiedName().equals(classEff)) {
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
        // Removes the class itself from its dependencies
        dstList.remove(src);
        for (Granule dst: dstList) {
            dependencies.add(new Dependency(src, dst, type));
        }
        return dependencies;
    }

    /**
     * Filters a set of class names by removing pieces of information that does not belong to the actual fullyQualifiedName.
     * This means the <...> genericity indicator and the [] array indicator.
     * @param classNames The Set to filter.
     * @return THe filtered Set.
     */
    private Set<String> filterClassName(Set<String> classNames){
        return classNames.stream()
                .map( className -> className.replaceAll("(<.*>)|(\\[])", ""))
                .collect(Collectors.toSet());
    }

    /**
     * Recursively explore and adds the parameters of a generic type reference to a set of dependencies.
     * @param typeBinding The type that has arguments to explore.
     * @param dependencies The dependency Set to add the dependencies in.
     */
    private void extractTypeArguments(ITypeBinding typeBinding, Set<String> dependencies){
        if(!typeBinding.isParameterizedType()) return;
        for(ITypeBinding parameterBinding : typeBinding.getTypeArguments()){
            dependencies.add(parameterBinding.getQualifiedName());
            extractTypeArguments(parameterBinding, dependencies);
        }
    }




    /* ******************************************** */
    /* *************** VISIT METHOD *************** */
    /* ******************************************** */

    @Override
    public boolean visit(TypeDeclaration node) {
        // Find dependencies from extends and implements declarations
        ITypeBinding typeBinding = node.resolveBinding();
        // Ignore internal and anonymous classes
        if (typeBinding.isNested()) return false;
        List<ITypeBinding> bindings = new ArrayList<>(Arrays.asList(typeBinding.getInterfaces()));
        if (typeBinding.getSuperclass() != null) {
            bindings.add(typeBinding.getSuperclass());
        }
        for (ITypeBinding binding : bindings) {
            rawInheritanceDependencies.add(binding.getQualifiedName());
        }
        return true;
    }

    @Override
    public boolean visit(EnumDeclaration node) {
        // Find dependencies from interfaces implemented by this enum.
        for (ITypeBinding binding : node.resolveBinding().getInterfaces()) {
            rawInheritanceDependencies.add(binding.getQualifiedName());
        }
        return true;
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        // Find dependencies from fields declaration
        ITypeBinding typeBinding = node.getType().resolveBinding();
        if(typeBinding != null) {
            rawAssociationDependencies.add(typeBinding.getQualifiedName());
            extractTypeArguments(typeBinding, rawAssociationDependencies);
        }
        return true;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        // Find dependencies from parameters, return type and exceptions thrown by a method
        IMethodBinding methodBinding = node.resolveBinding();
        if(methodBinding != null) {
            List<ITypeBinding> bindings = new ArrayList<>();
            bindings.addAll(Arrays.asList(methodBinding.getParameterTypes()));
            bindings.addAll(Arrays.asList(methodBinding.getExceptionTypes()));
            bindings.add(methodBinding.getReturnType());
            for (ITypeBinding binding : bindings) {
                rawUseLinkDependencies.add(binding.getQualifiedName());
                extractTypeArguments(binding, rawUseLinkDependencies);
            }
        }
        return true;
    }

    @Override
    public boolean visit(ClassInstanceCreation node) {
        // Find dependencies from a class instance creation
        ITypeBinding typeBinding = node.resolveTypeBinding();
        if(typeBinding != null) {
            rawUseLinkDependencies.add(typeBinding.getQualifiedName());
        }
        return true;
    }

    @Override
    public boolean visit(VariableDeclarationStatement node) {
        // Find dependencies from a variable declaration
        ITypeBinding typeBinding = node.getType().resolveBinding();
        if(typeBinding != null) {
            rawUseLinkDependencies.add(typeBinding.getQualifiedName());
            extractTypeArguments(typeBinding, rawUseLinkDependencies);
        }
        return true;
    }

    @Override
    public boolean visit(MethodInvocation node) {
        // Find dependencies from a method call
        IMethodBinding methodBinding = node.resolveMethodBinding();
        if(methodBinding != null && methodBinding.getDeclaringClass() != null) {
            rawUseLinkDependencies.add(methodBinding.getDeclaringClass().getQualifiedName());
        }
        return true;
    }

    @Override
    public boolean visit(FieldAccess node) {
        // Find dependencies from a field access (except static ones)
        IVariableBinding fieldBinding = node.resolveFieldBinding();
        if(fieldBinding != null && fieldBinding.getDeclaringClass() != null) {
            rawUseLinkDependencies.add(fieldBinding.getDeclaringClass().getQualifiedName());
        }
        return true;
    }
    
    @Override
    public boolean visit(SimpleName node) {
        // Find dependencies from a static field access
        IBinding binding = node.resolveBinding();
        if(binding != null && binding.getKind() == IBinding.VARIABLE) {
            IVariableBinding variableBinding = (IVariableBinding) binding;
            if(variableBinding.isField()){
                ITypeBinding declaringClass = variableBinding.getDeclaringClass();
                if(declaringClass != null){
                    String className = declaringClass.getQualifiedName();
                    if(!className.equals(currentAnalyzedClassName)){
                        rawUseLinkDependencies.add(className);
                    }
                }
            }
        }
        return true;
    }
}
