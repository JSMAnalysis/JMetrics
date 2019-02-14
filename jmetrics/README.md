# Jmetrics

## Build instructions :

This project uses **Gradle** as a build tool. To ensure Gradle's version compatibility, a wrapper 
is provided.

For Windows, please use the **gradlew.bat** script.

For Linux and MacOS, please use the **gradlew** script.

To compile and test, you can use the following commands :

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

To run you can use the following commands :

- to run the application with Gradle: 

```
./gradlew run --args="<PATH>"
```

- to run the application from the binary jar :

```
java -jar jmetrics.jar <PATH>
```

Where `PATH` is the path to the location of the project you want to analyze.