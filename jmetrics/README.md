# Jmetrics

Jmetrics is a Java application designed to analyze other Java applications.
It aims to provide some indicators of a Java project's quality.

It can analyze both source and byte code to :

 - extract dependencies between classes and packages in the project.
 
 - compute some software Metrics base on those defined by Robert C. Martin in
 <a href="https://linux.ime.usp.br/~joaomm/mac499/arquivos/referencias/oodmetrics.pdf">this article</a>

It can then output dependency graphs using the .DOT format and Metrics values using the .CSV
format.

## Build instructions :

This project uses **Gradle** as a build tool. To ensure Gradle's version compatibility, a wrapper 
is provided. To use it, please run the wrapper script corresponding to your operating system :

 - **gradlew.bat** for Windows.

 - **gradlew** for Linux, MacOS or other Unix-like systems.

The following examples assume that you're using an Unix-like system ; you will have to replace the *./gradlew* by 
*.\gradlew.bat* for it to work on Windows. To compile and test, you can use the following commands :

- to compile the project : 

```
./gradlew compileJava
```

- to compile tests : 

```
./gradlew compileTestJava
```

- to run compile and run tests : 

```
./gradlew test
```

- to compile everything and run tests : 

```
./gradlew build
```

- to compile the project and generate a jar binary : 

```
./gradlew jar
```

All compiler outputs will be located in the **out/** folder.
The application binaries are located in **out/bin/**.
The tests binaries are located in **out/test/**
The jar is located in **out/jar/**

## Run instructions :

Jmetrics allows to analyze either source or bytecode of a Java project.
Be aware the bytecode analysis is pretty much limited and should be used when the
 source code is unavailable only. The dependencies that can be extracted from bytecode 
 are mainly the followings :
 
- inheritance and implementation (except parameterized types).
- simple attributes (no parameterized types or arrays).
- methods parameters, return type and thrown exceptions (except parameterized types).

For now, the recommended way to run jmetrics is using gradle :

```
./gradlew run --args="<args...>"
```

The command-line application takes 2 mandatory arguments. These are the following :

 - `-p <root> | --path <root>` The path to the root of the Java project you want to analyze 
 (containing .class or .java files).
  
 - `-t <source|bytecode> | --type <source|bytecode>`. This tells the application whether to
 performs a source code or bytecode analysis. Value can only be source or bytecode.
 
For example, to analyze this project (sources are located in **src/**), you can use:

```
./gradlew run --args="-p src -t source"
```

It will parse the project's files and generate some outputs that will be placed in 
 $current_directory$/report/. These outputs are composed of :
 
  - Metrics .csv files for class and package level analysis.
  
  - dependency graph .dot files for class and package level analysis.

You can change the output directory by providing a `-o` or `--output` option. For example,
you can choose to put outputs in the output folder by using the following command :

```
./gradlew run --args="-p src -t source -o output"
```

You can also restrict the analysis to a subdirectory of the project. This can be done using
the `-s` or `--subdir` option. Note that you will still have to provide the project's root.
For example, to only analyze the *presentation* package, you can restrict the analysis to the
corresponding directory :

```
./gradlew run --args="-p src -t source -s src/fr/ubordeaux/jmetrics/presentation"
```

This document may not cover all the available options. To get a description of these
options, just use the `-h` or  `--help` option like this :

```
./gradlew run --args="-h"
```
