package fr.ubordeaux.jmetrics.analysis;

import fr.ubordeaux.jmetrics.metrics.ClassGranule;
import fr.ubordeaux.jmetrics.metrics.Granule;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An implementation of the {@link CouplingParser} interface that uses introspection.
 */
public class IntrospectionCouplingParser extends IntrospectionParser implements CouplingParser {

    @Override
    public List<Dependency> getDependencies(ClassFile srcFile) {
        List<Dependency> dependencies = new ArrayList<>();
        dependencies.addAll(getAggregationDependencies(srcFile));
        dependencies.addAll(getInheritanceDependencies(srcFile));
        dependencies.addAll(getUseLinkDependencies(srcFile));
        return dependencies;
    }

    /**
     * Retrieve efferent dependencies of type Inheritance of a given class.
     * @param srcFile The file to analyze.
     * @return The list of Inheritance dependencies.
     */
    private List<Dependency> getInheritanceDependencies(ClassFile srcFile) {
        Class<?> srcClass = getClassFromFile(srcFile);

        Class<?> superClass = srcClass.getSuperclass();
        Class<?>[] interfaces = srcClass.getInterfaces();

        ArrayList<Class<?>> efferentDependencies = new ArrayList<>(Arrays.asList(interfaces));
        if (superClass != null) {
            efferentDependencies.add(superClass);
        }

        List<ClassFile> projectClasses = ProjectStructure.getInstance().getClasses();
        List<Granule> dstClasses = findEfferentDependenciesInProject(efferentDependencies, projectClasses);
        return generateDependenciesList(new ClassGranule(srcFile), DependencyType.Inheritance, dstClasses);
    }

    /**
     * Retrieve efferent dependencies of type Association of a given class.
     * @param srcFile The file to analyze.
     * @return The list of Association dependencies.
     */
    private List<Dependency> getAggregationDependencies(ClassFile srcFile) {
        Class<?> srcClass = getClassFromFile(srcFile);

        ArrayList<Class<?>> efferentDependencies = new ArrayList<>();
        for (Field field: srcClass.getDeclaredFields()) {
            Class<?> type = field.getType();
            if (!type.isPrimitive() && !efferentDependencies.contains(type) && srcClass != type) {
                efferentDependencies.add(type);
            }
        }

        List<ClassFile> projectClasses = ProjectStructure.getInstance().getClasses();
        List<Granule> dstClasses = findEfferentDependenciesInProject(efferentDependencies, projectClasses);
        return generateDependenciesList(new ClassGranule(srcFile), DependencyType.Association, dstClasses);
    }

    /**
     * Retrieve efferent dependencies of type UseLink of a given class.
     * @param srcFile The file to analyze.
     * @return The list of UseLink dependencies.
     */
    private List<Dependency> getUseLinkDependencies(ClassFile srcFile) {
        Class<?> srcClass = getClassFromFile(srcFile);

        ArrayList<Class<?>> efferentDependencies = new ArrayList<>();
        ArrayList<Class<?>> methodDependencies = new ArrayList<>();
        for (Method method: srcClass.getDeclaredMethods()) {
            Class<?>[] exceptions = method.getExceptionTypes();
            Class<?>[] parameters = method.getParameterTypes();
            Class<?> returnType = method.getReturnType();

            methodDependencies.addAll(Arrays.asList(exceptions));
            methodDependencies.addAll(Arrays.asList(parameters));
            methodDependencies.add(returnType);
        }

        for (Class<?> dep: methodDependencies) {
            if (!dep.isPrimitive() && !efferentDependencies.contains(dep) && srcClass != dep) {
                efferentDependencies.add(dep);
            }
        }

        List<ClassFile> projectClasses = ProjectStructure.getInstance().getClasses();
        List<Granule> dstClasses = findEfferentDependenciesInProject(efferentDependencies, projectClasses);
        return generateDependenciesList(new ClassGranule(srcFile), DependencyType.UseLink, dstClasses);
    }

    /**
     * Determine the list of efferent dependencies class that is present in project classes.
     * @param efferentDependencies List of class where the analyzed class depend upon.
     * @param projectClasses List of all class files in the project.
     * @return List of Granule (Elementary) such as efferent dependencies is in the project.
     */
    private List<Granule> findEfferentDependenciesInProject(List<Class<?>> efferentDependencies,
                                                            List<ClassFile> projectClasses) {
        List<Granule> matchDependencies = new ArrayList<>();
        for (Class<?> classEff: efferentDependencies) {
            if(classEff.isMemberClass() || classEff.isLocalClass()){ //ignores inner and anonymous classes
                continue;
            }
            for (ClassFile dstFile: projectClasses) {
                Class<?> dstClass = getClassFromFile(dstFile);
                if (dstClass.getName().equals(classEff.getName())) {
                    Granule dst = new ClassGranule(dstFile);
                    matchDependencies.add(dst);
                }
            }
        }
        return matchDependencies;
    }

    /**
     * Generate a list of dependencies from a source granule, a dependency type and a list of destination granules.
     * @param src The source granule of the dependencies.
     * @param type The type of the dependencies.
     * @param dstList The list of destination class granules.
     * @return The list of generated dependencies.
     */
    private List<Dependency> generateDependenciesList(Granule src, DependencyType type, List<Granule> dstList) {
        List<Dependency> dependencies = new ArrayList<>();
        for (Granule dst: dstList) {
            dependencies.add(new Dependency(src, dst, type));
        }
        return dependencies;
    }

}
