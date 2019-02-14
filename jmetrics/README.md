# Jmetrics

## Build instructions :

This project uses **Gradle** as a build tool. To ensure Gradle's version compatibility, a wrapper 
is provided. To use it, please run the wrapper script corresponding to your operating system :

 - **gradlew.bat** for Windows.

 - **gradlew** for Linux, MacOS or other Unix-like systems.

The following example assume that you're using an Unix-like system ; you will have to replace the *./gradlew* by 
*.\gradlew.bat* for it to work on Windows. To compile and test, you can use the following commands :

- to compile the project : 

```
./gradlew compileJava
```

- to compile tests : 

```
./gradlew compileTestJava
```

- to run tests : 

```
./gradlew test
```

- to compile everything and run tests : 

```
./gradlew build
```

- to generate a jar binary : 

```
./gradlew jar
```

All compiler outputs will be located in the **out/** folder.
The application binaries are located in **out/bin/**.
The tests binaries are located in **out/test/**
The jar is located in **out/jar/**

## Run instructions :

For now, jmetrics allows to display metrics values for all classes of a compiled Java project, as well as getting a
DOT-formatted dependency graph. The software detects dependencies in :
 
- inheritance and implementation
- attributes
- methods parameters, return type and thrown exceptions
 
You can run the application using the following command (using the jar compiled in out/jar/) :

```
java -jar jmetrics.jar <args...>
```

Alternatively, you can run jmetrics using gradle :

```
./gradlew run --args="<args...>"
```

The command-line application takes up to 2 arguments, one mandatory and one optional. These arguments must come in
 the following order :

 - The path to the root of a compiled Java project (containing .class files). This is the project you want to analyze.
  This cannot be omitted.
  
 - A `--dot-only` flag. If this flag is passed to the application, it will only input a dependency graph encoded in the
 .DOT format. It is useful the redirect the program's output to a .dot file. This is optional.
 
For example, to analyze a project located in **out/bin/** and get **metrics values + DOT graph**, you can use :

```
java -jar jmetrics.jar out/bin/
```

To get a .dot file containing the dependency graph of the same project, you can use :

```
java -jar jmetrics.jar out/bin/ --dot-only > myfile.dot
```